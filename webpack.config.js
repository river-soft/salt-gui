var path = require('path');
var webpack = require('webpack');
var autoprefixer = require('autoprefixer');
var precss = require('precss');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

const NODE_ENV = process.env.NODE_ENV || 'develop';


module.exports = {
    devtool: 'cheap-module-eval-source-map',
    entry: [
        'webpack-hot-middleware/client?reload=true',
        'babel-polyfill',
        './client/index'
    ],
    output: {
        path: path.join(__dirname, '/src/main/resources/static/'),
        filename: '/js/bundle.js'
    },
    // watch: NODE_ENV == 'develop',
    // watchOptions: {
    //     aggregateTimeout: 100
    // },
    plugins: [
        new webpack.EnvironmentPlugin('NODE_ENV'),
        new webpack.optimize.OccurrenceOrderPlugin(),
        new webpack.HotModuleReplacementPlugin(),
        new ExtractTextPlugin('/css/main.css', {allChunks: true}),
        // new webpack.ProvidePlugin({
        //     $: "jquery/dist/jquery.min.js",
        //     jQuery: "jquery/dist/jquery.min.js",
        //     "window.jQuery": "jquery/dist/jquery.min.js"
        // })
        // new webpack.optimize.UglifyJsPlugin({
        //     warnings: false,
        //     drop_console: true,
        //     unsafe: true
        // })
        // new webpack.NoErrorsPlugin()
    ],
    module: {
        preLoaders: [
            {
                test: /\.js$/,
                loaders: ['eslint'],
                include: [
                    path.resolve(__dirname, "client")
                ]
            }
        ],
        loaders: [
            {
                loaders: ['react-hot', 'babel-loader'],
                include: [
                    path.resolve(__dirname, 'client')
                ],
                test: /\.js$/,
                plugins: ['transform-runtime']
            },
            {
                test: /\.(woff|eot|ttf|woff2)$/,
                loader: 'file?name=/fonts/[name].[ext]'
            },
            {
                test: /\.scss$/,
                loader: ExtractTextPlugin.extract('css!sass!autoprefixer')
            }
        ]
    }
};