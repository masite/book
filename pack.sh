#!/bin/sh
#! 创建补丁文件
#! DapVersion     基带版本
#! DversionName   目标版本
#! 文件生成后主要   app/build/outputs/tpatch-debug/update-升级版本.json         (多个分开)
#!                app/build/outputs/tpatch-debug/patch目标版本@升级版本.json   (多个分开)

app_version=$(cat build.gradle | grep 'APP_VERSION')
app_version=${app_version#*\"}
app_version=${app_version%\"*}

base_version=$(cat build.gradle | grep 'APP_BASE_VERSION')
base_version=${base_version#*\"}
base_version=${base_version%\"*}

if [ -z "$base_version" ]
then
    echo "$base_version 为空不进行操作"
else
    ./gradlew clean assembleDebug -DapVersion=$base_version -DversionName=$app_version
fi