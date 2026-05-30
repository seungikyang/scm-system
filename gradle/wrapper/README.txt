gradle-wrapper.jar 바이너리는 이 환경에서 생성할 수 없어 포함되지 않았다.
아래 명령으로 wrapper jar 를 생성해야 빌드가 가능하다 (로컬에 Gradle 8.5 설치 필요):

    gradle wrapper --gradle-version 8.5

또는 IDE(IntelliJ/Eclipse)에서 Gradle 프로젝트로 import 하면 자동 생성된다.
gradle-wrapper.properties / gradlew / gradlew.bat 는 이미 작성되어 있다.
