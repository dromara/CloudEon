
#!/usr/bin/env sh

# 确保脚本抛出遇到的错误
set -e


push_addr=https://pandas886:${GITHUB_TOKEN}@github.com/Pandas886/cloudeon-website.git # git提交地址，也可以手动设置，比如：push_addr=git@github.com:xugaoyi/vuepress-theme-vdoing.git
commit_info=`git describe --all --always --long`
dist_path=docs/.vuepress/dist # 打包生成的文件夹路径
push_branch=master # 推送的分支

# 生成静态文件
npm run build

# 进入生成的文件夹
cd $dist_path
git config --global user.email "Pandas886@example.com"
git config --global user.name "Pandas886"
git init
git remote add github $push_addr
git add -A
git commit -m "deploy, $commit_info"
git push -f $push_addr HEAD:$push_branch

cd -
rm -rf $dist_path