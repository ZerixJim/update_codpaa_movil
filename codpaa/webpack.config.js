
const path = require('path');
const webpack = require('webpack');

module.exports = {

    entry: './administracion/script/main.js',
    output : {

        path : path.resolve(__dirname, 'administracion/app'),

        filename: 'bundle-main.js'

    },


    plugins: [new webpack.ProvidePlugin({

        $:'jquery',
        jQuery:'jquery'


    })],

    module: {
        rules: [

            {
                test: /\.css$/,
                use:['style-loader', 'css-loader']
            }
        ]
    }
};