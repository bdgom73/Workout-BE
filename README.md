# WORKOUT APP V1 
* [Workout Application V2 이동](https://github.com/bdgom73/exersiceV2) 
* [Workout Application V1 Frontend 이동](https://github.com/bdgom73/exersiceV2)
## 1. 회원관리

### 1-1 로그인 
* **URL** 
```TEXT
/myApi/member/login?keep=false
```
* **파라미터** (method = **POST**)
  
`파라미터를 FORMDATA 형식으로 전달합니다.`

|파라미터|타입|필수여부|설명|
|:---:|:---:|:---:|---|
|email|string|<span style="color : #4b89dc;">**true**</span>|로그인시 필요한 이메일 아이디를 가져옵니다.
|password|string|<span style="color : #4b89dc;">**true**</span>|로그인시 필요한 패스워드를 가져옵니다.
|keep|boolean|<span style="color : #4b89dc;">**true**</span>|로그인 유지시간을 설정합니다. ( 24시간 유지시 true 그렇지 않으면 fasle)

* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|string|회원토큰
|message|string|전달 메시지
|result_state|boolean|성공여부


### 1-2 회원가입
* **URL** 
```TEXT
/myApi/member/signup
```
* **파라미터** (method = **POST**)
   
`파라미터를 JSON 형식으로 전달합니다.`

|파라미터|타입|필수여부|설명|
|:---:|:---:|:---:|---|
|email|string|<span style="color : #4b89dc;">**true**</span>|회원가입시 가입할 이메일 아이디를 가져옵니다.
|password1|string|<span style="color : #4b89dc;">**true**</span>|회원가입시 가입할 패스워드를 가져옵니다.
|password2|string|<span style="color : #4b89dc;">**true**</span>|회원가입시 확인할 패스워드를 가져옵니다.
|name|string|<span style="color : #4b89dc;">**true**</span>|회원가입시 가입할 이름을 가져옵니다.
|nickname|string|<span style="color : #4b89dc;">**true**</span>|회원가입시 가입할 별명을 가져옵니다.

* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|string|성공시 success 그렇지 않으면 null
|message|string|전달 메시지
|result_state|boolean|성공여부

### 1-3 로그인 회원정보
* **URL** 
```TEXT
/myApi/member/information
```
* **파라미터** (method = **GET**)
   
`파라미터를 HTTP Header [Authorization]으로 토큰을 전달합니다.`

* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|json|회원정보데이터를 리턴합니다.
|**DATA [Member]**|
|exp|string|만료시간
|id|long|회원 식별번호
|email|string|회원 이메일
|name|string|회원 이름
|avatar_url|string|회원 아바타URL
|rank|string|회원 등급

|필드|타입|설명|
|:---:|:---:|---|
|message|string|전달 메시지
|result_state|boolean|성공여부

### 1-4 유저 찾기
#### ✔ 단일유저찾기
* **URL** 
```TEXT
/myApi/member/find?type=검색타입&term=검색내용
```
* **파라미터** (method = **GET**)
   
`파라미터를 queryString 형식으로 전달합니다.`

|파라미터|타입|필수여부|설명|
|:---:|:---:|:---:|---|
|type|string|<span style="color : #4b89dc;">**true**</span>|type이 **id**면 식별번호를 통해 찾고<br> **nickname**이면 닉네임을 통해 찾습니다.
|term|string|<span style="color : #4b89dc;">**true**</span>|찾고싶은 식별번호 값 또는 닉네임을 전달합니다.


* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|json|회원정보데이터를 리턴합니다.
|**DATA [Member]**|
|id|long|회원 식별번호
|email|string|회원 이메일
|name|string|회원 이름
|avatar_url|string|회원 아바타URL
|rank|string|회원 등급

|필드|타입|설명|
|:---:|:---:|---|
|message|string|전달 메시지
|result_state|boolean|성공여부

#### ✔ 복수유저찾기

* **URL** 
```TEXT
/myApi/member/findall?condition=조건&term=조건값&size=가져올수&limit=페이지

[default] : /myApi/member/findall?size=가져올수&limit=페이지
```
* **파라미터** (method = **GET**)
   
`파라미터를 queryString 형식으로 전달합니다.`

|파라미터|타입|필수여부|설명|
|:---:|:---:|:---:|---|
|condition|string|<span style="color : #4b89dc;">**true**</span>|조건이 **rank** 이면 해당 등급의 회원을 가져옵니다 <br> 조건이 **default[null]** 이면 전체 회원을 가져옵니다.
|term|string|<span style="color : #4b89dc;">**true**</span>|조건이 rank 이면 등급을 선택할 수 있습니다.
|size|string|<span style="color : #4b89dc;">**true**</span>|가져올 회원의 수를 선택합니다
|limit|string|<span style="color : #4b89dc;">**true**</span>|가져올 회원의 페이지를 선택합니다.


* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|list|회원정보데이터를 리턴합니다.
|**DATA [Member]**|
|id|long|회원 식별번호
|email|string|회원 이메일
|name|string|회원 이름
|avatar_url|string|회원 아바타URL
|rank|string|회원 등급

|필드|타입|설명|
|:---:|:---:|---|
|message|string|전달 메시지
|result_state|boolean|성공여부

### 1-5 내정보
* **URL** 
```TEXT
/myApi/member/me
```
* **파라미터** (method = **GET**)
   
`파라미터를 HTTP Header [Authorization]으로 토큰을 전달합니다.`

* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|json|회원정보데이터를 리턴합니다.
|**DATA [Member]**|
|id|long|회원 식별번호
|age|Integer|회원 나이
|height|double|회원 키
|weight|double|회원 몸무게
|SMM|double|회원 골격근량

|필드|타입|설명|
|:---:|:---:|---|
|message|string|전달 메시지
|result_state|boolean|성공여부

---

## 2. 운동관리

### 2-1 단일검색
* **URL** 
```TEXT
/myApi/workout/find?wid=운동식별아이디
```
* **파라미터** (method = **GET**)
   
`파라미터를 queryString 형식으로 전달합니다.`

|파라미터|타입|필수여부|설명|
|:---:|:---:|:---:|---|
|wid|string \| long|<span style="color : #4b89dc;">**true**</span>|운동 식별아이디값을 전달합니다.


* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|object|운동 정보를 리턴합니다.
|**DATA [Workout]**|
|id|long|운동 식별번호
|name|string|운동 이름
|workout_img|string|운동 이미지URL
|part|string|운동 부위
|e_type|string|유산소 \| 무산소 여부
|explain|string|운동 설명

|필드|타입|설명|
|:---:|:---:|---|
|message|string|전달 메시지
|result_state|boolean|성공여부

### 2-2 다중검색

* **URL** 
```TEXT
/myApi/workout/findall?condition=조건&term=조건값&size=가져올수&page=페이지
```
* **파라미터** (method = **GET**)
   
`파라미터를 queryString 형식으로 전달합니다.`

|파라미터|타입|필수여부|설명|
|:---:|:---:|:---:|---|
|condition|string|<span style="color : #4b89dc;">**true**</span>|조건이 **type** 이면 운동 타입으로 검색합니다 <br> 조건이 **part** 이면 운동 부위로 검색합니다. <br> 조건이 **default[null]** 이면 조건이 없습니다.
|term|string|<span style="color : #4b89dc;">**true**</span>|조건에 맞는 검색어를 전달합니다.
|size|string|<span style="color : #4b89dc;">**true**</span>|가져올 운동의 수를 전달합니다.
|page|string|<span style="color : #4b89dc;">**true**</span>|가져올 현재 페이지를 전달합니다.


* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|list|운동 리스트 정보를 리턴합니다.
|**DATA [Workout]**|
|id|long|운동 식별번호
|name|string|운동 이름
|workout_img|string|운동 이미지URL
|part|string|운동 부위
|e_type|string|유산소 \| 무산소 여부
|explain|string|운동 설명

|필드|타입|설명|
|:---:|:---:|---|
|message|string|전달 메시지
|result_state|boolean|성공여부



---

## 3. 루틴관리

### 3-1 단일검색
* **URL** 
```TEXT
/myApi/workout/get/routine/{routine_id}
```
* **파라미터** (method = **GET**)
   
`파라미터를 path 형식으로 전달합니다.`

|파라미터|타입|필수여부|설명|
|:---:|:---:|:---:|---|
|routine_id|string \| long|<span style="color : #4b89dc;">**true**</span>|운동 식별아이디값을 전달합니다.


* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|object|운동 정보를 리턴합니다.
|**DATA [Routine]**|
|id|long|루틴 식별번호
|title|string|루틴 제목
|part|string|운동 부위
|share|boolean|공유여부
|member_id| string \| long|회원 식별번호
|member_email|string|회원 이메일 아이디
|member_name|string|회원 이름
|originalAuthor|string \| long|원작자정보
|createdDate|string \| localdatetime|작성 시간
|modifiedDate|string \| localdatetime|수정 시간
|volumes|list|볼륨 정보
|**volumes [Volume]**|
|id|string \| long|볼륨 식별번호
|num|Integer|운동 횟수
|sets|Integer|운동 세트수
|workout|object|운동정보
|**workout [Workout]**|
|id|string \| long|운동 식별번호
|name|string|운동 이름
|workout_img|string|운동 이미지URL
|part|string|운동 부위
|e_type|string|유산소 \| 무산소 여부
|explain|string|운동 설명


|필드|타입|설명|
|:---:|:---:|---|
|message|string|전달 메시지
|result_state|boolean|성공여부

### 3-2 개인복수검색
* **URL** 
```TEXT
/myApi/workout/get/list/routine
```
* **파라미터** (method = **GET**)
   
`파라미터를 HTTP Header [Authorization]으로 토큰을 전달합니다.`

* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|list|운동 정보를 리턴합니다.
|**DATA [Routine]**|
|id|long|루틴 식별번호
|title|string|루틴 제목
|part|string|운동 부위
|share|boolean|공유여부
|member_id| string \| long|회원 식별번호
|member_email|string|회원 이메일 아이디
|member_name|string|회원 이름
|originalAuthor|string \| long|원작자정보
|createdDate|string \| localdatetime|작성 시간
|modifiedDate|string \| localdatetime|수정 시간
|volumes|list|볼륨 정보
|**volumes [Volume]**|
|id|string \| long|볼륨 식별번호
|num|Integer|운동 횟수
|sets|Integer|운동 세트수
|workout|object|운동정보
|**workout [Workout]**|
|id|string \| long|운동 식별번호
|name|string|운동 이름
|workout_img|string|운동 이미지URL
|part|string|운동 부위
|e_type|string|유산소 \| 무산소 여부
|explain|string|운동 설명

---

## 4. 캘린더관리

### 4-1 범위검색
* **URL** 
```TEXT
/myApi/calendar/get/all/{start}/{end}
```
* **파라미터** (method = **GET**)
   
`파라미터를 path 형식으로 전달합니다.`

|파라미터|타입|필수여부|설명|
|:---:|:---:|:---:|---|
|start|date|<span style="color : #4b89dc;">**true**</span>|처음 범위 날짜를 전달합니다.
|end|date|<span style="color : #4b89dc;">**true**</span>|마지막 범위 날짜를 전달합니다.

* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|object|캘린더 정보를 제공합니다.
|**DATA [Calendar]**|
|id|long|캘린더 메모 식별번호
|title|string|캘린더 메모 제목
|memo|string|캘린더 메모
|color|string|캘린더 바 색코드
|start|date|처음 범위 날짜
|end|date|마지막 범위 날짜
|member|object|회원정보

|필드|타입|설명|
|:---:|:---:|---|
|message|string|전달 메시지
|result_state|boolean|성공여부

---

## 5. 검색관리

### 5-1 루틴검색
* **URL** 
```TEXT
/myApi/search?size=사이즈&page=페이지
```
* **파라미터** (method = **POST**)
   
`파라미터를 queryString | FormData 형식으로 전달합니다.`

|파라미터|타입|필수여부|설명|
|:---:|:---:|:---:|---|
|keywords|list|<span style="color : #4b89dc;">**true**</span>|검색 키워드를 전달합니다.
|size|Integer|<span style="color : #4b89dc;">**true**</span>|검색 크기를 전달합니다.
|page|Integer|<span style="color : #4b89dc;">**true**</span>|검색 페이지를 전달합니다.

* **출력결과** `[JSON]`

|필드|타입|설명|
|:---:|:---:|---|
|data|object|캘린더 정보를 제공합니다.
|**DATA**|
|total|Integer \| long |전체 게시글 수
|content|list|`[공유된 루틴]`검색 결과 리스트


|필드|타입|설명|
|:---:|:---:|---|
|message|string|전달 메시지
|result_state|boolean|성공여부
