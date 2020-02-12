import React, { PureComponent } from 'react'
import { withCookies } from 'react-cookie'
import getSymbolFromCurrency from 'currency-symbol-map'

import './Styles/App.css'
import Selectable from './Components/Selectable'
import Loader from './Components/Loader'

import CurrencyFormatter from './Utils/CurrencyFormatter'
import { Colors, LINEAR_BACKGROUND_COLOR, LINEAR_BOX_BACKGROUND_COLOR } from './Theme/Colors'

class Item extends PureComponent {
  render () {
    return (<div className={this.props.selected ? 'selected' : 'unselected'}>{this.props.children}</div>)
  }
}

class App extends PureComponent {

  static defaultProps = {
    id: 0,
    locked: false,
    focused: false,
    value: '0',
    error: '',
    label: '0',
    type: 'tel',
  }

  constructor (props) {
    super(props)
    const { cookies } = props
    this.initState = {
      inputValue: '',
      outputValue: '',
    }
    this.state = {
      currencies: [],
      csrfToken: cookies.get('XSRF-TOKEN'),
      ...this.initState,
      inputCurrency: '',
      outputCurrency: '',
      message: 0,
      missingValue: false,
      missingInputCurrency: false,
      missingOutputCurrency: false,
      loading: false
    }
    this.timer = []
    this.startInterval = this.startTimer.bind(this)
    this.stopInterval = this.stopTimer.bind(this)
  }

  onChange = (input, e) => {
    if (!e.target.validity.valid) {
      this.setState({ missingValue: true }, () => this.startInterval('missingValue'))
      return
    }
    this.setState({ [input]: e.target.value })
  }

  startTimer (type) {
    if (this.timer[type]) this.stopInterval(type)
    this.timer[type] = setInterval(() => this.setState({ [type]: false }), 300)
  }

  stopTimer (type) {
    clearInterval(this.timer[type])
  }

  reset = () => {
    this.setState({ ...this.initState })
  }

  componentDidMount () {
    fetch('/currencies', {
      method: 'get',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
    }).then(response => {
      return response.json()
    }).then(data => {
      this.setState({ currencies: data })
    }).catch(e =>

      this.setState({ error: 'Request Error: ' + e }
      ))

  }

  ApiPost = () => {
    const {
      inputCurrency,
      outputCurrency,
      inputValue,
      csrfToken,
    } = this.state

    if (!inputValue.length) {
      this.setState({ missingValue: true }, () => this.startInterval('missingValue'))
      return
    } else if (!inputCurrency.length) {
      this.setState({ missingInputCurrency: true }, () => this.startInterval('missingInputCurrency'))
      return
    } else if (!outputCurrency.length) {
      this.setState({ missingOutputCurrency: true }, () => this.startInterval('missingOutputCurrency'))
      return
    }
    this.setState({ loading: true })
    fetch('/convert', {
      method: 'post',
      headers: {
        'Accept': 'application/json',
        'csrf-token': csrfToken,
        'x-xsrf-token': csrfToken,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        'from': inputCurrency,
        'to': outputCurrency,
        'value': inputValue
      })
    }).then(response => {
      return response.json()
    }).then(data => {
      let result = CurrencyFormatter.format(data.result, { currency: outputCurrency })
      this.setState({ outputValue: result, loading: false })
    }).catch(e => this.setState({ message: 'Request Error:' + e, loading: false }))
  }

  handleSelection = (input, key) => {
    this.state.currencies[key] && this.setState({ [input]: this.state.currencies[key].name })

  }

  render () {
    const {
      inputValue,
      outputValue,
      currencies,
      missingValue,
      missingInputCurrency,
      missingOutputCurrency,
      loading
    } = this.state
    const { label } = this.props
    if (currencies.length === 0) {
      return (
        <div className="App" style={LINEAR_BACKGROUND_COLOR}>
          <Loader><h3>Trying to fetch data...</h3></Loader>
        </div>
      )
    }
    return (
      <div className="App" style={LINEAR_BACKGROUND_COLOR}>
        <div className="box-container">
          <div className="box-item" style={LINEAR_BOX_BACKGROUND_COLOR}>
            <div className="input-wrapper">
              <input
                id={1}
                value={inputValue}
                autoFocus={true}
                placeholder={label}
                className="input"
                pattern="^-?[0-9]\d*\.?\d*$"
                style={{
                  color: !missingValue ? Colors.white : Colors.red,
                  transition: 'all .2s ease',
                  WebkitTransition: 'all .2s ease',
                  MozTransition: 'all .2s ease'
                }}
                onChange={(e) => this.onChange('inputValue', e)}
              />

            </div>
            <div className="drop-down-wrapper"
                 style={{
                   'background-color': !missingInputCurrency ? Colors.trans : Colors.boxTopColorLinearStart,
                   transition: 'all .2s ease  max-width 1s',
                   WebkitTransition: 'all .2s ease',
                   MozTransition: 'all .2s ease'
                 }}>
              <div className="selection-wrapper"
                   style={{
                     height: '100%', overflowY: 'scroll'
                   }}>
                <Selectable class={'selectable'} onClick={(e) => this.handleSelection('inputCurrency', e)}>
                  {currencies.map((item, i) => (
                    <Item key={i}>
                      <i className={'currency-symbol'}
                         style={{ color: Colors.white }}>{getSymbolFromCurrency(item.name)}</i>
                      <i style={{ color: Colors.white }}>{item.name}</i>
                    </Item>
                  ))}
                </Selectable>
              </div>
            </div>
          </div>
          <div className="btn">
            <button onClick={this.reset} className="reset-btn" type="reset"><p>&#x21bb;</p></button>
            <button onClick={this.ApiPost} className="submit-btn"><p>&#8595;</p>
              <p>&#8593;</p></button>
          </div>
          <div className="box-item">
            <div className="input-wrapper">
              <input
                disabled={true}
                value={outputValue}
                placeholder={label}
                className="input"
                style={{ color: Colors.red }}
              />
            </div>
            <div className="drop-down-wrapper"
                 style={{
                   'background-color': !missingOutputCurrency ? Colors.trans : Colors.boxTopColorLinearStart,
                   transition: 'all .2s ease  max-width 1s',
                   WebkitTransition: 'all .2s ease',
                   MozTransition: 'all .2s ease'
                 }}>
              <div className="selection-wrapper" style={{ height: '100%', overflowY: 'scroll' }}>
                <Selectable class={'selectable-output'} onClick={(e) => this.handleSelection('outputCurrency', e)}>
                  {currencies.map((item, i) => (
                    <Item key={i}>
                      <i className={'currency-symbol'}
                         style={{ color: Colors.red }}>{getSymbolFromCurrency(item.name)}</i>
                      <i style={{ color: Colors.red }}>{item.name}</i>
                    </Item>
                  ))}
                </Selectable>
              </div>
            </div>

          </div>
          {loading && <Loader/>}
        </div>

      </div>
    )
  }
}

export default withCookies(App)

