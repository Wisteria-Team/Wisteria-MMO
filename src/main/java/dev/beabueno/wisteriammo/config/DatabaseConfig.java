package dev.beabueno.wisteriammo.config;


import lombok.Value;

@Value
public class DatabaseConfig {
    String uri;
    String user;
    String pw;
}
