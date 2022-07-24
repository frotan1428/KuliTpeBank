package com.tpebank.dto.response;

import com.tpebank.dto.RecipientDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecipientListResponse {
   private List<RecipientDTO> recipients=new ArrayList<>();
}
