import React from 'react'
import './../Styles/App.css'
import Logo from './../Assets/logo.svg'

function ShowDetail (props) {
  return (
    <div className="center">
      <div className="App-logo">
        <img alt={'logo'} src={Logo}/>
        {props.children}
      </div>
    </div>
  )
}

export default ShowDetail
