package com.lcoil.params.git;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname GitEventParams
 * @Description TODO
 * @Date 2022/5/17 8:29 AM
 * @Created by l-coil
 */
@NoArgsConstructor
@Data
public class GitEventParams {
    @JsonProperty("zen")
    private String zen;
    @JsonProperty("hook_id")
    private Integer hookId;
    @JsonProperty("hook")
    private HookDTO hook;
    @JsonProperty("repository")
    private RepositoryDTO repository;
    @JsonProperty("sender")
    private SenderDTO sender;

}
