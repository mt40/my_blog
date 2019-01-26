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
      },
      run_test: {
        command: 'sbt test'
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
    },

    copy: {
      dev: {
        files: [
          {
            src: 'target/scala-2.12/scalajs-bundler/main/*-fastopt-bundle.js',
            dest: 'build/js/bundle.js'
          }
        ]
      },
      production: {
        files: [
          {
            src: 'target/scala-2.12/scalajs-bundler/main/*-opt-bundle.js',
            dest: 'build/js/bundle.js'
          }
        ]
      }
    },

    // https://www.npmjs.com/package/grunt-size-report
    size_report: {
      images: {
        options: {
          header: 'Images size report'
        },
        files: {
          list: ['assets/images/**/*']
        }
      },
      css: {
        options: {
          header: 'CSS size report'
        },
        files: {
          list: ['build/css/*.css']
        }
      },
      js: {
        options: {
          header: 'JavaScript size report'
        },
        files: {
          list: ['build/js/*.js']
        }
      },
      content: {
        options: {
          header: 'Content (blog posts) size report',
          showStatistics: true
        },
        files: {
          list: ['posts/**/*']
        }
      }
    }
  });

  // load plugins
  grunt.loadNpmTasks('grunt-shell');
  grunt.loadNpmTasks('grunt-sass');
  grunt.loadNpmTasks('grunt-size-report');
  grunt.loadNpmTasks('grunt-contrib-copy');

  // for dev, run `grunt dev`
  // for production, run `grunt production`
  var dev = ['shell:scalajs_dev', 'sass', 'copy:dev'];
  var production = [
    'shell:scalajs_production', 'shell:run_test', 'sass', 'copy:production', 'size_report'
  ];
  grunt.registerTask('dev', dev);
  grunt.registerTask('production', production);
  grunt.registerTask('build_sass', ['sass']);

};