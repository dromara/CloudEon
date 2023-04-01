#!/bin/bash
function absPath() {
	dir="$1"
	case "$(uname)" in
	Linux)
		abs_path=$(readlink -f "$dir")
		;;
	*)
		abs_path=$(
			cd "$dir" || exit
			pwd
		)
		;;
	esac
	#
	echo "$abs_path"
}
bin_abs_path=$(absPath "$(dirname "$0")")
base=$(absPath "$bin_abs_path/../")
java -jar  -Dspring.config.location=$base/conf/application.properties -Dcloudeon.home.path=$base $base/lib/cloudeon-server-1.0.0.jar