# DBMS (h2) 생성
데이터베이스 파일 생성 방법
jdbc:h2:~/jpashop (최소 한번)
~/jpashop.mv.db 파일 생성 확인
이후 부터는 jdbc:h2:tcp://localhost/~/jpashop 이렇게 접속

# DB 생성
application.yaml 파일에서 아래 값이면 자동 생성됨.
  ddl-auto: create



#QueryDSL 사용 순서
1. 아래명령어로, build 디렉토리에 관련 클래스 생성
gradlew build

2. 생성된 소스 확인
build/generated/querydsl

3. 소스에서 사용.


#build
gradlew clean build

#실행
cd build/libs
java -jar  jpashop-0.0.1-SNAPSHOT.jar



