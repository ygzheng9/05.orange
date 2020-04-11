const gulp = require('gulp');
const sourcemaps = require('gulp-sourcemaps');
const babel = require('gulp-babel');
const uglify = require('gulp-uglify-es').default;
const changed = require('gulp-changed');

const del = require('del');

const concat = require('gulp-concat');
const concatCss = require('gulp-concat-css');

// 使用到的配置信息
const cfg = {
    jsSrc: './js/**/*.js',
    jsTarget: '../webapp/assets/js'
};

// const jsTarget = './dist';

// 2020/04/05
// 1. 只处理 js es2015 -> es5 ，并且是单文件，不打包；
// 2. 使用 layui 的 模块机制；在 ./js/lay-config.js 中配置模块的目录；
// 3. 如果要打包，使用 webpack；

// 增加 changed, 只处理更新的文件；
function compile() {
    return gulp.src(cfg.jsSrc)
        .pipe(changed(cfg.jsTarget))
        .pipe(sourcemaps.init())
        .pipe(
            babel({
                presets: ['@babel/preset-env']
            })
        )
        .pipe(gulp.dest(cfg.jsTarget))
        .pipe(sourcemaps.write('.'));
}
exports.compile = compile;

function build() {
    return gulp.src(cfg.jsSrc)
        .pipe(
            babel({
                presets: ['@babel/preset-env']
            })
        )
        .pipe(uglify())
        .pipe(gulp.dest(cfg.jsTarget));
}
exports.build = build;

function clean(cb){
    del(['../webapp/assets/js/**/*', '!../webapp/assets/js'], { force: true });
    cb();
}
exports.clean = clean;


function watch(done) {
    gulp.watch(cfg.jsSrc, { delay: 500 }, compile);

    done()
}
exports.watch = watch;
