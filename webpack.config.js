var path = require('path');
var webpack = require('webpack');
var autoprefixer = require('autoprefixer');
var precss = require('precss');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

const NODE_ENV = process.env.NODE_ENV || 'develop';

module.exports = {
    //TODO: перед заливом засскоментить
    devtool: 'cheap-module-eval-source-map',
    entry: [
        'webpack-dev-server/client',
        'webpack/hot/only-dev-server',
        //TODO: перед заливом засскоментить
        'webpack-hot-middleware/client?reload=true',
        'babel-polyfill',
        './client/index'
    ],
    output: {
        path: path.join(__dirname, '/src/main/resources/static/'),
        filename: '/js/bundle.js'
    },
    node: {
        net: 'empty',
        fs: 'empty',
        dns: 'empty',
        tls: 'empty'
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
        //TODO: перед заливом расскоментить
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
                ],
                exclude: /node_modules/,
            },
        ],
        loaders: [
            {
                loaders: ['react-hot', 'babel-loader'],
                include: [
                    path.resolve(__dirname, 'client')
                ],
                test: /\.js$/,
                plugins: ['transform-runtime'],
                exclude: /node_modules/,
            },
            {
                test: /\.(woff|eot|ttf|woff2)$/,
                loader: 'file?name=/fonts/[name].[ext]'
            },
            {
                test: /\.(png|jpg|svg)$/,
                loader: 'file?name=/img/[name].[ext]'
            },
            {
                test: /\.scss$/,
                loader: ExtractTextPlugin.extract('css!sass!autoprefixer')
            },
            {
                test: /\.json$/,
                loader: 'json-loader'
            }
        ]
    },

    devServer: {
        host: '192.168.1.110',
        port: '8000',
        hot: true,
        proxy: {
            '/': {
                path: /.*/,
                target: 'http://localhost:8082'
            }
        }
    }
};