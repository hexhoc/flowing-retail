package io.flowing.retail.checkout.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Data
public class Item {
  private String articleId;
  private int amount;
}
