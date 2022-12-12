package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {}

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu(MenuGroup menuGroup, List<Product> products) {
        Menu menu = new Menu(name, price, menuGroup);
        List<MenuProduct> allMenuProducts = menuProducts.stream()
                .map(request -> request.toMenuProducts(menu, products))
                .collect(toList());
        menu.create(allMenuProducts);

        return menu;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> toProductIds() {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());
    }
}