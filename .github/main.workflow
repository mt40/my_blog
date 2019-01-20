workflow "Build and deploy" {
  on = "push"
  resolves = ["deploy"]
}

action "deploy" {
  uses = "./.github/action_deploy/"
}
