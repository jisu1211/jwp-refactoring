package kitchenpos.ui;

import kitchenpos.ControllerTest;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TableGroupRestController 테스트")
@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends ControllerTest {

    @MockBean
    private TableGroupService tableGroupService;

    private TableGroup 단체_테이블;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private List<OrderTable> 주문_테이블_목록;

    @BeforeEach
    public void setUp() {
        super.setUp();

        단체_테이블 = new TableGroup();
        단체_테이블.setId(1L);

        주문테이블1 = new OrderTable(1L, null, 4, true);
        주문테이블2 = new OrderTable(2L, null, 4, true);

        주문_테이블_목록 = Arrays.asList(주문테이블1, 주문테이블2);
        단체_테이블.setOrderTables(주문_테이블_목록);
    }

    @Test
    void 테이블_그룹_등록() throws Exception {
        given(tableGroupService.create(any(TableGroup.class))).willReturn(단체_테이블);

        webMvc.perform(post("/api/table-groups")
                    .content(mapper.writeValueAsString(단체_테이블))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(단체_테이블.getId().intValue())))
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
    }

    @Test
    void 테이블_그룹_등록_실패() throws Exception{
        given(tableGroupService.create(any(TableGroup.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/table-groups")
                    .content(mapper.writeValueAsString(단체_테이블))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 테이블_그룹_해제() throws Exception {
        webMvc.perform(delete("/api/table-groups/" + 단체_테이블.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void 테이블_그룹_해제_실패() throws Exception {
        doThrow(new IllegalArgumentException()).when(tableGroupService).ungroup(단체_테이블.getId());

        webMvc.perform(delete("/api/table-groups/" + 단체_테이블.getId()))
                .andExpect(status().isBadRequest());
    }
}