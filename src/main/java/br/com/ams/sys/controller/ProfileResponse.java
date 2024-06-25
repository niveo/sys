package br.com.ams.sys.controller;

import java.util.Set;

public record ProfileResponse(String username, Set<String> roles) {
}