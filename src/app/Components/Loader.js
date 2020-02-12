import React from 'react'
import './../Styles/App.css'
// eslint-disable-next-line import/no-webpack-loader-syntax
import LoadingLogo from '../Assets/logo.svg'

const ShowLoading = (props) => {
  return (
    <div className="loading-container">
      <img alt={'logo'} src={LoadingLogo}/>
      {props.children}
    </div>
  )
}

export default ShowLoading
