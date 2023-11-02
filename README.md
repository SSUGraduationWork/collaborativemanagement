# collaborativemanagement
팀플을 하는 과정을 투명하게 공개함으로써 팀원들의 참여도를 높이고, 보다 객관적인 평가가 가능한 협업 관리 도구입니다.

## Github flow 작성 양식

``` text
브랜치 생성 양식
{작업타입}/#{이슈번호-issue keyword}
ex)feature/#11-websocketHandler
```

### commit 생성 양식
ex) [Feat] 새로운 기능 추가
ex) [Fix] 버그 수정
``` text
#11 [Feat] 새로운 기능 추가
#11 [Fix] 버그 수정
#11 [Docs] 문서 수정
#11 [Test] 테스트 코드 추가
#11 [Refactor] 코드 리팩토링, 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우, 파일을 삭제하는 작업만 수행한 경우
#11 [Chore] 코드 외 빌드 부분 혹은 패키지 매니저 수정사항
```

### Pull Request 

무엇을 구현 했는지 및 최대한 세세하게 적기
* 참고 레퍼펀스

### 기능

`board-service` 
`calendar-service` 
`user-service` 
`dashboard-service` 
`work-service`

- work-service는 Node.js이므로 아래 명령어로 실행

```
npm i
npm start
```

### 웹 서비스
|대시보드|작업|파일|
|----|----|----|
|<img width="1470" alt="대시보드1" src="https://github.com/SSUGraduationWork/collaborativemanagement/assets/125520029/b5e61b1b-2343-4fd7-9c6c-8003c850a490">|<img width="1470" alt="작업" src="https://github.com/SSUGraduationWork/collaborativemanagement/assets/125520029/2f7a84d8-a5b0-4543-adff-21bb0207c59c">|<img width="1470" alt="파일" src="https://github.com/SSUGraduationWork/collaborativemanagement/assets/125520029/48b9e13d-a42c-420a-8f08-0e84087ee958">|

|캘린더|채팅|기여도|
|----|----|----|
|<img width="1470" alt="캘린더" src="https://github.com/SSUGraduationWork/collaborativemanagement/assets/125520029/67706a37-d8db-4478-aab0-4c9e91b2ca97">|<img width="1470" alt="채팅" src="https://github.com/SSUGraduationWork/collaborativemanagement/assets/125520029/08728c13-e4f7-465f-aad8-ddbb3c87f9a6">|<img width="1470" alt="기여도" src="https://github.com/SSUGraduationWork/collaborativemanagement/assets/125520029/b90888c7-2d3a-4e38-bdd9-0a6e4b42ba17">|

### 참고 링크
