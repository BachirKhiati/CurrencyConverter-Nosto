const webpack = require('webpack')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const path = require('path')

module.exports = {
  plugins: [new MiniCssExtractPlugin({
    filename: './src/main/resources/static/main.css'
  })],
  module: {
    rules: [
      {
        test: path.join(__dirname, '.'),
        exclude: /(node_modules)/,
        loader: 'babel-loader',
        query: {
          presets: ['@babel/preset-react'],
          plugins: ['@babel/plugin-proposal-class-properties']
        }
      },
      {
        test: /\.css$/i,
        use: [MiniCssExtractPlugin.loader, 'css-loader']
      },
      {
        test: /\.svg$/,
        use: [{
          loader: 'file-loader',
          options: {
            jsx: true,
            outputPath: './src/main/resources/static',
            publicPath: './'
          }
        }]
      }
    ]
  },
  cache: true,
  entry: './src/index.js',
  mode: 'development',
  output: {
    filename: './src/main/resources/static/built/bundle.js',
    path: __dirname
  }
}
