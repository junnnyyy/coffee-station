package ssafy.runner.controller.partner;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ssafy.runner.domain.dto.ResultResponseDto;
import ssafy.runner.domain.dto.menu.*;
import ssafy.runner.domain.enums.UserType;
import ssafy.runner.service.MenuService;
import ssafy.runner.service.MenuSizeService;
import ssafy.runner.util.CustomPrincipal;

@RestController
@Api(tags = {"메뉴 관련 API"})
@RequiredArgsConstructor
@RequestMapping("/api/partner/menu")
public class MenuController {

    private final MenuService menuService;
    private final MenuSizeService menuSizeService;

    @PostMapping("")
    @ApiOperation(value = "메뉴 생성")
    public MenuResponseDto createMenu(Authentication authentication, @RequestBody MenuCreateRequestDto requestDto) {
        System.out.println(requestDto);
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        if (principal.getRole().equals(UserType.CUSTOMER.toString())) throw new RuntimeException("점주가 아니면 메뉴를 생성할 수 없습니다.");
        return menuService.createMenu(
            principal.getEmail(),
            requestDto.getCategoryId(),
            requestDto.getPrice(),
            requestDto.getName(),
            requestDto.getImgUrl(),
            requestDto.isSignature());
    }

    @GetMapping("")
    @ApiOperation(value = "메뉴 리스트 조회")
    public MenuListResponseDto findMenuList(Authentication authentication) {
        // 가게용 조회이므로
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        if (principal.getRole().equals(UserType.CUSTOMER.toString()))
            throw new IllegalStateException("점주가 아니면 메뉴를 조회할 수 없습니다.");

        return menuService.findShopMenuList(principal.getEmail());
    }

    @GetMapping("/{menuId}")
    @ApiOperation(value = "메뉴 단일 조회")
    public MenuResponseDto findOneMenu(Authentication authentication, @PathVariable("menuId") Long menuId) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        if (principal.getRole().equals(UserType.CUSTOMER.toString()))
            throw new IllegalStateException("점주가 아니면 메뉴를 조회할 수 없습니다.");
        return menuService.findShopMenu(principal.getEmail(), menuId);
    }

    @PutMapping("/{menuId}")
    @ApiOperation(value = "메뉴 수정")
    public MenuResponseDto updateMenu(Authentication authentication,
                                      @PathVariable("menuId") Long menuId,
                                      @RequestBody MenuUpdateRequestDto requestDto) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        if (principal.getRole().equals(UserType.CUSTOMER.toString()))
            throw new IllegalStateException("점주가 아니면 메뉴를 수정할 수 없습니다.");

        return menuService.updateMenu(
                principal.getEmail(),
                menuId,
                requestDto.getCategoryId(),
                requestDto.getName(),
                requestDto.getImgUrl(),
                requestDto.getPrice(),
                requestDto.isSignature());
    }

    @DeleteMapping("/{menuId}")
    @ApiOperation(value = "메뉴 삭제")
    public ResultResponseDto deleteMenu(Authentication authentication,
                                        @PathVariable("menuId") Long menuId) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        if (principal.getRole().equals(UserType.CUSTOMER.toString()))
            throw new IllegalStateException("점주가 아니면 메뉴를 삭제할 수 없습니다.");

        return menuService.deleteMenu(principal.getEmail(), menuId);
    }

    @PostMapping("/{menuId}/size")
    @ApiOperation(value = "메뉴 사이즈 생성")
    public MenuSizeResponseDto createMenuSize(Authentication authentication,
                                              @PathVariable("menuId") Long menuId,
                                              @RequestBody MenuSizeCreateRequestDto requestDto) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        if (principal.getRole().equals(UserType.CUSTOMER.toString()))throw new IllegalStateException("점주가 아니면 메뉴를 생성할 수 없습니다.");

        return menuSizeService.createMenuSize(principal.getEmail(), menuId, requestDto.getSizeId(), requestDto.getPrice());
    }

    @PutMapping("/{menuId}/size")
    @ApiOperation(value = "메뉴 사이즈 수정")
    public MenuSizeResponseDto updateMenuSize(Authentication authentication,
                                                  @PathVariable("menuId") Long menuId,
                                                  @RequestBody MenuSizeUpdateRequestDto requestDto) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        if (principal.getRole().equals(UserType.CUSTOMER.toString()))throw new IllegalStateException("점주가 아니면 메뉴를 생성할 수 없습니다.");

        return menuSizeService.updateMenuSize(principal.getEmail(), requestDto.getMenuSizeId(), menuId, requestDto.getSizeId(), requestDto.getPrice());
    }

    @DeleteMapping("/{menuId}/size/{sizeId}")
    @ApiOperation(value = "메뉴 사이즈 삭제")
    public ResponseEntity<String> removeMenuSize(Authentication authentication,
                                         @PathVariable("menuId") Long menuId,
                                         @PathVariable("sizeId") Long sizeId) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        if (principal.getRole().equals(UserType.CUSTOMER.toString()))throw new IllegalStateException("점주가 아니면 메뉴를 생성할 수 없습니다.");

        String message = menuSizeService.deleteMenuSize(principal.getEmail(), menuId, sizeId);
        if (message.equals("삭제 완료")){
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{menuId}/status")
    @ApiOperation(value = "메뉴 상태 수정")
    public MenuResponseDto updateMenuStatus(Authentication authentication,
                                                  @PathVariable("menuId") Long menuId,
                                                  @RequestBody MenuStatusUpdateRequestDto requestDto) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        if (principal.getRole().equals(UserType.CUSTOMER.toString())) throw new IllegalStateException("점주가 아니면 메뉴상태를 변경할 수 없습니다.");
        return menuService.updateMenuStatus(principal.getEmail(), menuId, requestDto.getStatus());
    }
}
