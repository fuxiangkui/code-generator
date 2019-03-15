
//var glob = require("glob"); //多页面时用到
var path = require("path");
var webpack = require("webpack");
var merge = require('webpack-merge');
var chalk = require('chalk'); //添加颜色
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');//css独立打包
var CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin;//抽出公共JS
var CopyWebpackPlugin = require('copy-webpack-plugin'); //复制文件
var OpenBrowserPlugin = require('open-browser-webpack-plugin'); //自动打开浏览器


var px2rem       = require('postcss-px2rem'); //px转rem
var precss       = require('precss');

var autoprefixer = require('autoprefixer');  //自动加前缀如 -web-,-o-
var OptimizeCSSPlugin = require('optimize-css-assets-webpack-plugin');//css压缩
//var UglifyJSPlugin  =  require('uglifyjs-webpack-plugin');


var buildConfig = {
    env :  'prod' ,   //dev、sat、uat、fat、prod，默认prod  //运行环境标识
    //res :  '0'        //(0,1)默认1打包  是否打包resouse.js文件
};
const argv = require('yargs').argv;
console.info(chalk.cyan("=======参数 : [ "+argv._+" ] \n"));
//带参数(npm run prod fat 1)
if( argv._.length > 0 || process.env.NODE_ENV){
    buildConfig.env = argv._[0] || process.env.NODE_ENV || buildConfig.env;
    //buildConfig.res = String(argv._[1]) || buildConfig.res;
    console.info(chalk.cyan("运行环境    （'sat','uat','fat','prod'）:"+buildConfig.env +"\n"));
    //console.info(chalk.cyan("是否打包resouse.js文件（(0,1)默认1打包）:"+buildConfig.res +"\n"));
    var fs = require('fs');
    var filename = './src/buildConfig.json';
    fs.writeFileSync(filename, JSON.stringify(buildConfig));
}else{
    buildConfig.env = 'dev';
    //buildConfig.res = '0';
}
var srcName = '';
if( buildConfig.env == 'prod'){
    srcName = 'generator/';
}


var statics = srcName + 'static/';
var extractCSS = new ExtractTextPlugin(statics + 'css/[name].css?[contenthash]');//将js中引入的css分离的插件
//var CompressionWebpackPlugin = require('compression-webpack-plugin');//GZIP压缩
//var BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;//分析包体依赖

var baseConfig = {
    entry: {
        app: './src/main.js',
        'babel-polyfill': 'babel-polyfill',
        'vendor': ['vue','vuex','vue-router','axios','element-ui']
    },
    output: { //配置打包结果， path 定义了输出的文件夹，filename则定义了打包结果文件的名称
        path: path.join(__dirname,"./dist"), // 输出文件的保存路径
        publicPath : '/',
        filename: statics + 'js/[name].[hash:8].js', //只要再加上hash这个参数就可以了
        chunkFilename: statics + "js/chunk/[name].[chunkHash:8].js"
    },
    resolve: {
        extensions: ['.js', '.vue', '.json'],//自动扩展文件后缀名，意味着我们require模块可以省略不写后缀名
        alias: {
            'vue': 'vue/dist/vue.js',    //不加会报runtime-only错误
            '@': path.join(__dirname,'./src') //掉用模板时间用到
        }
    },
    module: {
        rules: [
            {test: /\.vue$/,loader: 'vue-loader'},
            {
              test: /\.js$/,
              exclude: /(node_modules|bower_components)/,
              use: {
                loader: 'babel-loader',
                options: {
                  presets: ['env']
                }
              }
            },
            //{ test: /\.css$|\.scss$/, loader: 'style-loader!css-loader!postcss-loader' },
            {test: /\.scss|\.css$/,use: ExtractTextPlugin.extract({fallback: 'style-loader?minimize',use: ['css-loader?minimize','sass-loader?minimize','postcss-loader?minimize']})},
            {test: /\.jpg$|\.jpeg$|\.gif$|\.png$/, loader: 'url-loader?limit=1024&name='+srcName+'static/image/[name].[hash:8].[ext]'},
            {test: /\.svg$|\.woff$|\.ttf$|\.eot$/, loader: 'url-loader?limit=1024&name='+srcName+'static/font/[name].[hash:8].[ext]'},
            {test: /\.json$/,loader: 'json-loader'}// JSON
        ]
    },
    plugins: [
        // new webpack.LoaderOptionsPlugin({
        //     options: {
        //         postcss: function () {
        //             //remUnit = 16 ， 16px = 1rem
        //             //remPrecision = 8 ,保留8位小数
        //             //autoprefixer 自动加前缀如 -web-,-o-
        //             //return [precss, autoprefixer,px2rem({remUnit:16,remPrecision:8})];
        //             //开发时取消
        //             return [precss, autoprefixer];
        //         }
        //     }
        // }),
        //生成html文件
        new HtmlWebpackPlugin({
            filename: srcName+'index.html',
            template: 'index.html',//启动页面
            inject: true,//要把script插入到标签里
            minify: {  //压缩配置
                removeComments: true, //删除html中的注释代码
                collapseWhitespace: true,  //删除html中的空白符
                removeAttributeQuotes: true  //删除html元素中属性的引号
            },
            chunksSortMode: 'dependency' //按dependency的顺序引入
        }),
        //css
        extractCSS,
        new OptimizeCSSPlugin({//压缩提取出的css，并解决ExtractTextPlugin分离出的js重复问题(多个文件引入同一css文件)
            cssProcessorOptions: {
                safe: true
            }
        }),
        //多个 html共用一个js文件(chunk)
        new CommonsChunkPlugin({
            name: ['vendor','common'],
            minChunks: Infinity
        }),
        // 调用模块的别名
        // new webpack.ProvidePlugin({
        //     $: "jquery",
        //     jQuery: "jquery",
        //     "window.jQuery": "jquery"
        // }),
        // new CompressionWebpackPlugin({ //GZIP压缩
        //     asset: '[path].gz[query]',
        //     algorithm: 'gzip',
        //     test: new RegExp('\\.(' +['js', 'css','html'].join('|') +')$'),
        //     threshold: 10240,
        //     minRatio: 0.8
        // }),
        //new BundleAnalyzerPlugin()//分析包体依赖

        new CopyWebpackPlugin([
            { from: './static/third-libs'   , to:srcName+ 'static',ignore: ['.*']},
        ])
    ]
}


var webpackConfig = null;
if(buildConfig.env === 'dev'){//开发环境
    console.info(chalk.green("=======开发环境======"+"\n"));
    const port = '9875'; //默认启动端口号
    webpackConfig = merge(baseConfig, {
        devtool : '#cheap-module-eval-source-map',
        devServer : {
            hot: true,
            inline: true,//实时刷新
            //colors: true,  //终端中输出结果为彩色
            host: "0.0.0.0",
            port: port,
            contentBase: './public',
            historyApiFallback: true,
            disableHostCheck: true,//新版的webpack-dev-server出于安全考虑，默认检查hostname，如果hostname不是配置内的，将中断访问。
            proxy: {//本地代理
                '/generator/*': {
                    target: 'http://localhost:3030/',
                    secure: false
                }
            }
        },
        plugins : [
            new webpack.HotModuleReplacementPlugin(),  //代码热替换
            new OpenBrowserPlugin({    //自动打开浏览器
                url: 'http://localhost:' + port
            })
        ]
    })
    module.exports = webpackConfig;//package.json script start : webpack-dev-server自动启动webpack
}else{//线上环境
    console.info(chalk.red("=======线上环境======\n"));
    webpackConfig = merge(baseConfig, {
        devtool : '#source-map',
        plugins : [
            //如果用 Webpack类似的打包工具时，生产状态会在 Vue 源码中由 process.env.NODE_ENV 决定，默认在开发状态。
            //new webpack.DefinePlugin({'process.env': {NODE_ENV: '"production"'}}),

            // new webpack.optimize.UglifyJsPlugin({// 压缩代码
            //     mangle: false,//是否要混淆
            //     compress: {//压缩配置
            //         warnings: false,// 不显示警告
            //         drop_debugger: false,
            //         drop_console: true
            //     },
            //     sourceMap: false  //是否生成sourceMap文件
            // })
        ]
    });
    // if( buildConfig.res == 1){
    //     console.info(chalk.red("=======是否加载resouse.js======" + buildConfig.res+"\n"));

    // }
    //手动启动webpack
    var ora = require('ora')
    var spinner = ora('=====开始编译中...');
    spinner.start();
    webpack(webpackConfig, function (err, stats) {
        spinner.stop();
        if (err) throw err
        process.stdout.write(stats.toString({
          colors: true,
          modules: false,
          children: false,
          chunks: false,
          chunkModules: false
        }) + '\n\n')
        console.log(chalk.cyan('=======编译完成.\n'))
    })
}


