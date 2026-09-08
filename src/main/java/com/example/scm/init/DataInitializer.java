package com.example.scm.init;

import com.example.scm.domain.Category;
import com.example.scm.domain.Item;
import com.example.scm.domain.Partner;
import com.example.scm.domain.User;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.repository.CategoryRepository;
import com.example.scm.repository.ItemRepository;
import com.example.scm.repository.PartnerRepository;
import com.example.scm.repository.UserRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 학습용 로그인 계정과 예시 데이터를 애플리케이션 시작 시 넣는다.
 *
 * <p>{@link CommandLineRunner} 구현체이므로 Spring Boot 기동이 끝난 뒤 {@code run()}이
 * 호출된다. 단, {@code scm.seed.enabled=true}일 때만 Bean이 만들어지고 사용자가 이미
 * 있으면 전체 작업을 건너뛴다. 운영형 MySQL 설정에서는 기본값이 false다.</p>
 *
 * <p>시드 계정의 공통 비밀번호는 {@code password1!}이며 DB에는 BCrypt 해시로 저장된다.</p>
 */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scm.seed", name = "enabled", havingValue = "true")
public class DataInitializer implements CommandLineRunner {

    private static final String SEED_PASSWORD = "password1!";

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // 여러 번 재시작해도 같은 예제 데이터가 중복 생성되지 않게 하는 간단한 가드다.
        if (userRepository.count() > 0) {
            log.info("[DataInitializer] 시드 데이터가 이미 존재합니다. skip.");
            return;
        }
        log.info("[DataInitializer] 시드 데이터를 생성합니다.");

        seedUsers();
        seedPartners();
        seedCategoriesAndItems();

        log.info("[DataInitializer] 시드 데이터 생성 완료.");
    }

    private void seedUsers() {
        String encoded = passwordEncoder.encode(SEED_PASSWORD);
        userRepository.save(User.builder()
                .email("admin@scm.com").password(encoded).name("관리자").role(UserRole.ADMIN).build());
        userRepository.save(User.builder()
                .email("manager@scm.com").password(encoded).name("매니저").role(UserRole.MANAGER).build());
        userRepository.save(User.builder()
                .email("user@scm.com").password(encoded).name("홍길동").role(UserRole.USER).build());
    }

    private void seedPartners() {
        partnerRepository.save(Partner.builder()
                .name("(주)베스트공급사").businessNumber("123-45-67890").partnerType(PartnerType.SUPPLIER)
                .contactName("김공급").phone("02-1234-5678").email("contact@best.com")
                .address("서울시 강남구 테헤란로 1").status(PartnerStatus.ACTIVE).build());
        partnerRepository.save(Partner.builder()
                .name("대한자재").businessNumber("234-56-78901").partnerType(PartnerType.SUPPLIER)
                .contactName("이자재").phone("031-222-3333").email("sales@daehan.com")
                .address("경기도 성남시 분당구 2").status(PartnerStatus.ACTIVE).build());
        partnerRepository.save(Partner.builder()
                .name("한빛유통").businessNumber("345-67-89012").partnerType(PartnerType.CUSTOMER)
                .contactName("박고객").phone("051-444-5555").email("buy@hanbit.com")
                .address("부산시 해운대구 3").status(PartnerStatus.ACTIVE).build());
        partnerRepository.save(Partner.builder()
                .name("종합상사글로벌").businessNumber("456-78-90123").partnerType(PartnerType.BOTH)
                .contactName("최양쪽").phone("02-777-8888").email("both@global.com")
                .address("서울시 마포구 4").status(PartnerStatus.ACTIVE).build());
    }

    private void seedCategoriesAndItems() {
        Category electronics = categoryRepository.save(Category.builder()
                .name("전자부품").description("저항, 콘덴서, IC 등 전자부품").build());
        Category packaging = categoryRepository.save(Category.builder()
                .name("포장재").description("박스, 완충재, 테이프 등 포장 자재").build());
        Category rawMaterial = categoryRepository.save(Category.builder()
                .name("원자재").description("금속, 플라스틱 원자재").build());

        // 전자부품
        itemRepository.save(item("ITM-001", "볼트 M6", electronics.getId(), "EA",
                new BigDecimal("150.00"), 100, ItemStatus.ACTIVE));
        itemRepository.save(item("ITM-002", "너트 M6", electronics.getId(), "EA",
                new BigDecimal("90.00"), 100, ItemStatus.ACTIVE));
        itemRepository.save(item("ITM-003", "세라믹 콘덴서 0.1uF", electronics.getId(), "EA",
                new BigDecimal("30.00"), 500, ItemStatus.ACTIVE));

        // 포장재
        itemRepository.save(item("ITM-101", "택배박스 중", packaging.getId(), "BOX",
                new BigDecimal("700.00"), 200, ItemStatus.ACTIVE));
        itemRepository.save(item("ITM-102", "에어캡 롤", packaging.getId(), "EA",
                new BigDecimal("3500.00"), 50, ItemStatus.ACTIVE));

        // 원자재
        itemRepository.save(item("ITM-201", "알루미늄 판재", rawMaterial.getId(), "KG",
                new BigDecimal("4200.00"), 30, ItemStatus.ACTIVE));
        itemRepository.save(item("ITM-202", "ABS 수지", rawMaterial.getId(), "KG",
                new BigDecimal("2800.00"), 40, ItemStatus.ACTIVE));

        // 단종 시연용 1건
        itemRepository.save(item("ITM-999", "구형 커넥터(단종)", electronics.getId(), "EA",
                new BigDecimal("500.00"), 0, ItemStatus.DISCONTINUED));
    }

    private Item item(String code, String name, Long categoryId, String unit,
                      BigDecimal unitPrice, int safetyStock, ItemStatus status) {
        return Item.builder()
                .itemCode(code).name(name).categoryId(categoryId).unit(unit)
                .unitPrice(unitPrice).safetyStock(safetyStock).status(status).build();
    }
}
