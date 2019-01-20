workflow "Build and deploy" {
  on = "push"
  resolves = ["deploy"]
}

action "deploy" {
  uses = "./action_deploy/",
  args = "Hello world"
}
