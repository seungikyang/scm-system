Gradle Wrapper 9.7.1의 실행 스크립트, 속성 파일, wrapper jar가 모두 저장소에 포함되어 있다.
시스템 Gradle 설치 없이 저장소 루트에서 다음 명령으로 확인한다:

    ./gradlew --version

Wrapper를 공식 배포본 기준으로 다시 생성해야 할 때만 다음 명령을 두 번 실행한다:

    ./gradlew wrapper --gradle-version=9.7.1 && ./gradlew wrapper
