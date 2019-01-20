#!/usr/bin/env bash

set -e

setup_git() {
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "Travis CI"
}

commit_website_files() {
  git rm -rf --cached -q . # clean existing tracked file list
  git checkout -b ${GITHUB_DEPLOY_BRANCH}
  git add -f "static.json" "index.html" "notfound.html" assets build posts
  git commit --message "Travis build: ${TRAVIS_BUILD_NUMBER}"
}

upload_files() {
  git remote add origin-deploy https://${GITHUB_TOKEN}@github.com/mt40/my_blog.git
  git push -u --set-upstream origin-deploy ${GITHUB_DEPLOY_BRANCH}
}

setup_git
commit_website_files
upload_files
