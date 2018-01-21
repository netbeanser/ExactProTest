const path = require('path');
const webpack = require('webpack');


var BUILD_DIR = path.resolve(__dirname, 'client/pages');
var APP_DIR = path.resolve(__dirname, 'client/app');

var config = {
  entry: APP_DIR + '/App.js',
  output: {
    path: BUILD_DIR,
    filename: 'bundle.js'
  },

  module : {

    rules : [ 
      {
        test : /\.jsx?$/,
        include : APP_DIR,
        exclude: /node_modules/,        
        use : 'babel-loader',

      }    

      ]
    }

};

module.exports = config;
