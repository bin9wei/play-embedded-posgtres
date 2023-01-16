package org.sample;

import lombok.Builder;

@Builder
public record Person(int personId, String firstName, String lastName, String address, String city) {
}
