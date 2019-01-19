// https://gruntjs.com/sample-gruntfile
// https://gist.github.com/jonschlinkert/9ffb288606727d4ef7f2

var sass = require('node-sass');

module.exports = function (grunt) {
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    // https://github.com/sindresorhus/grunt-shell
    shell: {
      options: {
        stdout: true,
        stderr: true,
        failOnError: true
      },
      scalajs_dev: {
        command: 'sbt fastOptJS::webpack'
      },
      scalajs_production: {
        command: 'sbt fullOptJS::webpack'
      }
    },

    // https://github.com/gruntjs/grunt-contrib-uglify
    uglify: {
      options: {
        banner: '/*! <%= grunt.template.today("yyyy-mm-dd") %> */\n',
        footer: '/* Author: minhthai */\n',
        compress: true,
        output: {
          comments: false
        }
      },
      production: {
        files: [
          {
            src: 'target/scala-2.12/scalajs-bundler/main/*-bundle.js',
            dest: 'build/js/bundle.js'
          }
        ]
      }
    },

    // https://github.com/sindresorhus/grunt-sass
    sass: {
      // options from 'node-sass' can also be used here
      // https://github.com/sass/node-sass#options
      options: {
        implementation: sass,
        sourceMap: false,
        outputStyle: "compact",
        sourceComments: false
      },
      production: {
        files: [
          {
            src: 'scss/*.scss',
            dest: 'build/css/index.css'
          }
        ]
      }
    }
  });

  // load plugins
  grunt.loadNpmTasks('grunt-shell');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-sass');

  // for dev, run `grunt dev`
  // for production, run `grunt production`
  grunt.registerTask('dev', ['shell:scalajs_dev', 'uglify', 'sass']);
  grunt.registerTask('production', ['shell:scalajs_production', 'uglify', 'sass']);

};