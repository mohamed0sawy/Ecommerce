const path = require('path');

module.exports = {
    entry: './src/main/resources/static/js/content-script.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'src/main/resources/static/dist/js'),
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env'],
                    },
                },
            },
        ],
    },
};
