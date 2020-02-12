import React, { Component } from 'react'
import ReactDOM from 'react-dom'

function isNodeInRoot (node, root) {
  while (node) {
    if (node === root) {
      return true
    }
    node = node.parentNode
  }
  return false
}

class Selectable extends Component {

  static defaultProps = {
    component: 'div',
    distance: 0,
    tolerance: 0,
    disabled: false,
  }

  constructor () {
    super()
    /**
     * This is stored outside the state, so that setting it doesn't
     * rerender the app during selection. shouldComponentUpdate() could work around that.
     * @type {Object}
     */
    this._mouseDownData = null
    this.state = {
      isBoxSelecting: false,
      persist: false,
      boxWidth: 0,
      boxHeight: 0,
      selectedItems: []
    }
    this._mouseUp = this._mouseUp.bind(this)
    this._selectElement = this._selectElement.bind(this)
    this._keyListener = this._keyListener.bind(this)

  }

  /**
   * Attach global event listeners
   */
  componentDidMount () {
    document.addEventListener('keydown', this._keyListener)
    document.addEventListener('keyup', this._keyListener)
  }

  /**
   * Remove global event listeners
   */
  componentWillUnmount () {
    document.removeEventListener('keydown', this._keyListener)
    document.removeEventListener('keyup', this._keyListener)
  }

  /**
   * Renders the component
   * @return {ReactComponent}
   */
  render () {
    let boxStyle = {
      position: 'absolute',
      zIndex: 9000,
      left: this.state.boxLeft,
      top: this.state.boxTop,
      width: this.state.boxWidth,
      height: this.state.boxHeight,
      cursor: 'default'
    }
    return (
      <this.props.component
        {...this.props}
        style={{
          position: 'relative',
          height: '100%',
          overflowY: 'auto',
          ...this.props.style
        }}
        onMouseDown={this._mouseDown}
      >
        {this.state.isBoxSelecting &&
        <div style={boxStyle} ref="selectbox"/>
        }
        {React.Children.map(this.props.children, (child, i) => {
          return React.cloneElement(child, {
            key: child.key || i,
            ref: 'selectable_' + child.key,
            selected: this.state.selectedItems.indexOf(child.key) > -1
          })
        }, this)}
      </this.props.component>
    )
  }

  /**
   * Called when a user presses the mouse button. Determines if a select box should
   * be added, and if so, attach event listeners
   */
  _mouseDown = (e) => {
    let node = ReactDOM.findDOMNode(this)
    let collides
    let offsetData
    let distanceData

    document.addEventListener('mouseup', this._mouseUp)

    // Right clicks
    if (e.nativeEvent.which !== 1) return

    if (!isNodeInRoot(e.target, node)) {
      distanceData = this._getDistanceData()
      offsetData = this._getBoundsForNode(node)
      collides = this._objectsCollide(
        {
          top: offsetData.top - distanceData.top,
          left: offsetData.left - distanceData.left,
          bottom: offsetData.offsetHeight + distanceData.bottom,
          right: offsetData.offsetWidth + distanceData.right
        },
        {
          top: e.pageY,
          left: e.pageX,
          offsetWidth: 0,
          offsetHeight: 0
        }
      )

      if (!collides) return
    }

    this._mouseDownData = {
      boxLeft: e.pageX - node.offsetLeft + node.scrollLeft,
      boxTop: e.pageY - node.offsetTop + node.scrollTop,
      nodeW: node.scrollWidth,
      nodeH: node.scrollHeight,
      initialX: e.pageX,
      initialY: e.pageY
    }

    e.preventDefault()

  }

  /**
   * Called when the user has completed selection
   */
  _mouseUp (e) {
    document.removeEventListener('mouseup', this._mouseUp)

    if (!this._mouseDownData) return
    let inRoot = isNodeInRoot(e.target, ReactDOM.findDOMNode(this))
    let click = (e.pageX === this._mouseDownData.initialX && e.pageY === this._mouseDownData.initialY)

    // Clicks outside the Selectable node should reset clear selection
    if (click && !inRoot) {
      this.setState({
        selectedItems: []
      })
      return this.props.onClick([])
    }

    // Handle selection of a single element
    if (click && inRoot) {
      return this._selectElement(e.pageX, e.pageY)
    }

    return null
  }

  /**
   * Selects a single child, given the x/y coords of the mouse
   * @param  {int} x
   * @param  {int} y
   */
  _selectElement = (x, y) => {
    let currentItems = this.state.selectedItems
    let index

    React.Children.forEach(this.props.children, child => {
      let node = ReactDOM.findDOMNode(this.refs['selectable_' + child.key])
      let collision = this._objectsCollide(
        node,
        {
          top: y,
          left: x,
          offsetWidth: 0,
          offsetHeight: 0
        },
        this.props.tolerance
      )

      if (collision) {
        index = currentItems.indexOf(child.key)
        if (this.state.persist) {
          if (index > -1) {
            currentItems.splice(index, 1)
          } else {
            currentItems.push(child.key)
          }
        } else {
          currentItems = [child.key]
        }
      }

    }, this)

    this._mouseDownData = null

    this.setState({
      isBoxSelecting: false,
      boxWidth: 0,
      boxHeight: 0,
      selectedItems: currentItems
    })
    this.props.onClick(Number(currentItems))

  }

  /**
   * Given a node, get everything needed to calculate its boundaries
   * @param  {HTMLElement} node
   * @return {Object}
   */
  _getBoundsForNode (node) {
    let rect = node.getBoundingClientRect()

    return {
      top: rect.top + document.body.scrollTop,
      left: rect.left + document.body.scrollLeft,
      offsetWidth: node.offsetWidth,
      offsetHeight: node.offsetHeight
    }
  }

  /**
   * Resolve the disance prop from either an Int or an Object
   * @return {Object}
   */
  _getDistanceData () {
    let distance = this.props.distance

    if (!distance) {
      distance = 0
    }

    if (typeof distance !== 'object') {
      return {
        top: distance,
        left: distance,
        right: distance,
        bottom: distance
      }
    }
    return distance
  }

  /**
   * Given two objects containing "top", "left", "offsetWidth" and "offsetHeight"
   * properties, determine if they collide.
   * @param  {Object|HTMLElement} a
   * @param  {Object|HTMLElement} b
   * @return {bool}
   */
  _objectsCollide (a, b, tolerance) {
    let aObj = (a instanceof HTMLElement) ? this._getBoundsForNode(a) : a
    let bObj = (b instanceof HTMLElement) ? this._getBoundsForNode(b) : b

    return this._coordsCollide(
      aObj.top,
      aObj.left,
      bObj.top,
      bObj.left,
      aObj.offsetWidth,
      aObj.offsetHeight,
      bObj.offsetWidth,
      bObj.offsetHeight,
      tolerance
    )
  }

  /**
   * Given offsets, widths, and heights of two objects, determine if they collide (overlap).
   * @param  {int} aTop    The top position of the first object
   * @param  {int} aLeft   The left position of the first object
   * @param  {int} bTop    The top position of the second object
   * @param  {int} bLeft   The left position of the second object
   * @param  {int} aWidth  The width of the first object
   * @param  {int} aHeight The height of the first object
   * @param  {int} bWidth  The width of the second object
   * @param  {int} bHeight The height of the second object
   * @return {bool}
   */
  _coordsCollide (aTop, aLeft, bTop, bLeft, aWidth, aHeight, bWidth, bHeight, tolerance) {
    if (typeof tolerance === 'undefined') {
      tolerance = 0
    }

    return !(
      // 'a' bottom doesn't touch 'b' top
      ((aTop + aHeight - tolerance) < bTop) ||
      // 'a' top doesn't touch 'b' bottom
      ((aTop + tolerance) > (bTop + bHeight)) ||
      // 'a' right doesn't touch 'b' left
      ((aLeft + aWidth - tolerance) < bLeft) ||
      // 'a' left doesn't touch 'b' right
      ((aLeft + tolerance) > (bLeft + bWidth))
    )
  }

  /**
   * Listens for the meta key
   */
  _keyListener (e) {
    this.setState({
      persist: !!e.metaKey
    })
  }
}

export default Selectable
