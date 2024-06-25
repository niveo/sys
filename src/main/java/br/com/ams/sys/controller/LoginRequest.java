package br.com.ams.sys.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password) {
}
