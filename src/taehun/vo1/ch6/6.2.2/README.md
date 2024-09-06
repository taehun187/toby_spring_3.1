## UserService 스프링 테스트 프로젝트

이 프로젝트는 **Spring Framework**를 사용하여 **트랜잭션 관리**와 **사용자 레벨 업그레이드 로직**을 구현하고, 이를 **JUnit**을 통해 테스트하는 예제입니다. 코드에는 **트랜잭션 관리**, **의존성 주입**, **모의(Mock) 테스트**가 포함되어 있습니다.

### 프로젝트 구조

- **TestServiceFactory**: 테스트에 필요한 데이터 소스, 트랜잭션 매니저, 메일 전송 등의 빈을 설정하는 스프링 구성 클래스입니다.
- **UserServiceTest**: 사용자 서비스의 기능을 테스트하는 JUnit 테스트 클래스입니다. 사용자 레벨 업그레이드 및 트랜잭션 처리 로직을 검증합니다.
- **MockMailSender**: 메일 전송을 모의로 처리하는 클래스입니다.
- **TestUserService**: 트랜잭션 롤백 기능을 테스트하기 위해 예외를 의도적으로 발생시키는 클래스입니다.
- **TestUserServiceException**: 롤백 시나리오를 테스트하기 위한 사용자 정의 예외 클래스입니다.

### 구성 요소 설명

1. **TestServiceFactory.java**
   - 스프링 테스트 환경을 위해 필요한 빈을 정의하는 설정 클래스입니다:
     - **DataSource**: 사용자 정보를 관리하기 위해 MySQL을 사용합니다.
     - **UserDaoJdbc**: 사용자 관리용 DAO(Data Access Object)입니다.
     - **UserService**: 사용자 서비스 로직을 처리하며, 레벨 업그레이드 기능을 포함합니다.
     - **TransactionManager**: 트랜잭션 경계를 관리합니다.
     - **DummyMailSender**: 테스트용 메일 발송 구현체입니다.

2. **UserServiceTest.java**
   - 사용자 서비스의 기능을 검증하는 테스트 메서드를 포함합니다:
     - **upgradeLevels()**: 특정 조건에 따라 사용자의 레벨이 올바르게 업그레이드되는지 테스트합니다.
     - **add()**: 사용자를 DB에 추가할 때, 레벨이 없는 사용자는 `BASIC` 레벨로 설정되는지 확인합니다.
     - **upgradeAllOrNothing()**: 사용자 레벨 업그레이드 중 예외가 발생할 때, 트랜잭션이 롤백되는지 확인하는 테스트입니다.

3. **MockMailSender.java**
   - **MailSender**의 모의 구현체로, 메일을 실제로 전송하지 않고 요청만 기록하여 테스트 목적으로 사용됩니다.

4. **TestUserService.java**
   - **UserServiceTx**의 확장 클래스로, 특정 사용자에 대해 레벨 업그레이드 도중 예외를 발생시켜 트랜잭션이 롤백되는지 확인하는 역할을 합니다.

### 주요 클래스 설명

- **TestServiceFactory**: 스프링 테스트를 위한 애플리케이션 컨텍스트 설정 클래스입니다.
- **UserServiceTest**: 트랜잭션 관리 및 사용자 서비스 기능을 검증하는 테스트 클래스입니다.
- **MockMailSender**: 메일 전송 요청을 모의 처리하는 테스트 클래스입니다.
- **TestUserService**: 예외 발생을 통해 트랜잭션 롤백을 테스트하는 클래스입니다.
