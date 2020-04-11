#!/bin/bash
# ----------------------------------------------------------------------
#  只打包应用的 jar 和前端，不打包 lib
# ----------------------------------------------------------------------

# 首先使用 mvn 编译打包: mvn clean package -Dmaven.test.skip=true
# 目的是解压后目录匹配
cd ./target/cake-release/

tar -czvf ../slim.tar.gz ./cake/config ./cake/webapp ./cake/lib/cake-1.0.jar

cd ../..


