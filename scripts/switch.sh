#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    # 하나의 문장을 만들어 파이프라인(|) 으로 넘겨주기 위해 echo 사용
    # nginx 가 변경할 프록시 주소 생성
    # 쌍따옴표 (")를 사용하지 않으면 $service_url을 그대로 인식하지 못하고 변수를 찾게 됨
    # sudo tee /etc/nginx/conf.d/service-url.inc => 앞에서 넘겨준 문장을 service-url.inc 에 덮어쓴다
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    echo "> 엔진엑스 Reload"
    sudo service nginx reload
}