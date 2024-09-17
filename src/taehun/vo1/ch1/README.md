다음은 해당 코드에 대한 `README.md` 파일 템플릿입니다.

```markdown
# User Management System

## 개요
이 프로젝트는 Java로 구현된 간단한 사용자 관리 시스템입니다. `UserDao` 클래스는 H2 데이터베이스를 사용하여 사용자 정보를 추가하고 조회하는 기능을 제공합니다. `User` 클래스는 사용자 정보를 담고 있는 객체입니다.

## 파일 구성
- `UserDao.java` : 데이터베이스와 상호작용하여 사용자를 추가하거나 조회하는 기능을 담당합니다.
- `User.java` : 사용자 객체로, id, name, password와 같은 정보를 포함하고 있습니다.

## 주요 기능
1. **사용자 추가 (`add`)**
   - `User` 객체를 데이터베이스에 저장합니다.
   - id, name, password 정보를 입력받아 데이터베이스에 삽입합니다.

2. **사용자 조회 (`get`)**
   - 사용자 id를 기반으로 해당 사용자의 정보를 데이터베이스에서 가져옵니다.
   - 조회된 사용자의 id, name, password 정보를 반환합니다.

## H2 데이터베이스
- 메모리 기반 H2 데이터베이스를 사용합니다.
- JDBC URL: `jdbc:h2:mem:testdb`
- 사용자명: `sa`
- 비밀번호: 없음 (빈 문자열)

## 클래스 설명
### UserDao.java
```java
public class UserDao {
    // 사용자 정보를 데이터베이스에 추가하는 메서드
    public void add(User user) throws ClassNotFoundException, SQLException { 
        // 상세 구현
    }

    // 사용자 정보를 ID로 조회하는 메서드
    public User get(String id) throws ClassNotFoundException, SQLException { 
        // 상세 구현
    }

    // H2 데이터베이스에 연결하는 메서드
    private Connection getConnection() throws ClassNotFoundException, SQLException { 
        // 상세 구현
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException { 
        // 프로그램 실행 메서드
    }
}
```

### User.java
```java
public class User {
    // 사용자 정보 클래스 (id, name, password 포함)
    private String id;
    private String name;
    private String password;
    
    // Getter, Setter 메서드
}
```

## 실행 방법
1. H2 데이터베이스 드라이버를 프로젝트에 추가합니다.
2. `UserDao` 클래스의 `main` 메서드를 실행하면 사용자 등록 및 조회가 가능합니다.

## 출력 예시
```
Lee 등록 성공
Taehun
married
Lee 조회 성공
```

## 요구 사항
- Java 8 이상
- H2 데이터베이스 드라이버


이 `README.md` 파일은 프로젝트의 주요 기능과 실행 방법에 대해 설명하고 있으며, 필요한 파일 및 클래스에 대한 설명도 포함하고 있습니다.
