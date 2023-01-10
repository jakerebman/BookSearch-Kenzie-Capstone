const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    minimize: false,
    usedExports: true
  },
  entry: {
//    examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js')
    bookmarkPage: path.resolve(__dirname, 'src', 'pages', 'bookmarkPage.js')
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    // https: false,
    https: true,
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
    port: 8080,
    open: true,
    // TODO: Changed localhost urls from http to https
    openPage: 'https://localhost:8080',
    // diableHostChecks, otherwise we get an error about headers and the page won't render
    disableHostCheck: true,
    contentBase: 'packaging_additional_published_artifacts',
    // overlay shows a full-screen overlay in the browser when there are compiler errors or warnings
    overlay: true,
    proxy: [
      {
        context: [
          '/bookmarks',
            // '/example',
        ],
        target: 'https://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/images'),
          to: path.resolve("dist/images")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}
