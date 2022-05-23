package io.flowing.retail.checkout.domain;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Customer {
  private String name;
  private String address;
}
