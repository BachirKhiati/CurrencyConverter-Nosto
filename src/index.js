import React from 'react'
import ReactDOM from 'react-dom'
import { CookiesProvider } from 'react-cookie'

import './app/Styles/Index.css'
import App from './app/App'
import * as serviceWorker from './app/serviceWorker'

ReactDOM.render(
  <CookiesProvider>
    <App/>
  </CookiesProvider>
  , document.getElementById('react'))

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister()
