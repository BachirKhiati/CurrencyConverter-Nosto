import React from 'react'
import './../Styles/App.css'
import Logo from './../Assets/logo.svg'

const ShowLoading = (props) => {
  return (
    <div className="loading-container">
      <img alt={'logo'} src={Logo}/>
      {props.children}
    </div>
  )
}

export default ShowLoading
