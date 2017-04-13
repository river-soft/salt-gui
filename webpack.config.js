var path = require('path');
var webpack = require('webpack');
var autoprefixer = require('autoprefixer');
var precss = require('precss');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

const NODE_ENV = process.env.NODE_ENV || 'develop';

module.exports = {
    //TODO: перед заливом закоментить
    devtool: 'cheap-module-eval-source-map',
    entry: [
        //TODO: перед заливом закоментить 3 строки
        'webpack-dev-server/client',
        'webpack/hot/only-dev-server',
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
    plugins: [
        new webpack.EnvironmentPlugin('NODE_ENV'),
        new webpack.optimize.OccurrenceOrderPlugin(),
        new webpack.HotModuleReplacementPlugin(),
        new ExtractTextPlugin('/css/main.css', {allChunks: true}),
        //TODO: перед заливом расскоментить все
        // new webpack.optimize.UglifyJsPlugin({
        //     warnings: false,
        //     drop_console: true,
        //     unsafe: true
        // }),
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
                test: /\.(jpe?g|png|gif|svg)$/i,
                loaders: [
                    'file-loader?hash=sha512&digest=hex&name=/img/[name].[ext]',
                    'image-webpack-loader'
                ]
            },
            {
                test: /\.scss$/,
                loader: ExtractTextPlugin.extract('css!sass!autoprefixer')
            },
            {
                test: /\.json$/,
                loader: 'json-loader'
            }
        ],
        imageWebpackLoader: {
            mozjpeg: {
                quality: 65
            },
            pngquant:{
                quality: "65-90",
                speed: 4
            },
            svgo:{
                plugins: [
                    {
                        removeViewBox: false
                    },
                    {
                        removeEmptyAttrs: false
                    }
                ]
            }
        }
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