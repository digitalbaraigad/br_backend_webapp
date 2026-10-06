package com.baraigad.ourwork.service;
import com.baraigad.ourwork.dto.OurWorkDto;
import com.baraigad.ourwork.entity.OurWork;
import com.baraigad.ourwork.repository.OurWorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor
public class OurWorkServiceImpl implements OurWorkService {
 private final OurWorkRepository repository;
 private OurWorkDto dto(OurWork w){ return OurWorkDto.builder().workId(w.getWorkId()).titleEn(w.getTitleEn()).titleMr(w.getTitleMr()).headingEn(w.getHeadingEn()).headingMr(w.getHeadingMr()).subHeadingEn(w.getSubHeadingEn()).subHeadingMr(w.getSubHeadingMr()).descriptionEn(w.getDescriptionEn()).descriptionMr(w.getDescriptionMr()).workTypeEn(w.getWorkTypeEn()).workTypeMr(w.getWorkTypeMr()).conservationStatusEn(w.getConservationStatusEn()).conservationStatusMr(w.getConservationStatusMr()).active(w.getActive()).mediaUrls(w.getMediaUrls()==null?new ArrayList<>():new ArrayList<>(w.getMediaUrls())).build(); }
 private void copy(OurWork w, OurWorkDto d){ w.setTitleEn(d.getTitleEn());w.setTitleMr(d.getTitleMr());w.setHeadingEn(d.getHeadingEn());w.setHeadingMr(d.getHeadingMr());w.setSubHeadingEn(d.getSubHeadingEn());w.setSubHeadingMr(d.getSubHeadingMr());w.setDescriptionEn(d.getDescriptionEn());w.setDescriptionMr(d.getDescriptionMr());w.setWorkTypeEn(d.getWorkTypeEn());w.setWorkTypeMr(d.getWorkTypeMr());w.setConservationStatusEn(d.getConservationStatusEn());w.setConservationStatusMr(d.getConservationStatusMr());w.setActive(d.getActive());w.setMediaUrls(d.getMediaUrls()==null?new ArrayList<>():new ArrayList<>(d.getMediaUrls()));w.setUpdatedDate(LocalDateTime.now()); }
 public OurWorkDto create(OurWorkDto d){ OurWork w=new OurWork();copy(w,d);w.setCreatedDate(LocalDateTime.now());w.setDelFlg(false);return dto(repository.save(w)); }
 public List<OurWorkDto> getAll(){return repository.findByDelFlgFalseOrderByCreatedDateDesc().stream().map(this::dto).toList();}
 public OurWorkDto get(Long id){return dto(find(id));}
 public OurWorkDto update(Long id, OurWorkDto d){OurWork w=find(id);copy(w,d);return dto(repository.save(w));}
 public OurWorkDto addMedia(Long id,List<String> urls){OurWork w=find(id);List<String> all=w.getMediaUrls()==null?new ArrayList<>():new ArrayList<>(w.getMediaUrls());all.addAll(urls);w.setMediaUrls(all);return dto(repository.save(w));}
 public void delete(Long id){OurWork w=find(id);w.setDelFlg(true);w.setUpdatedDate(LocalDateTime.now());repository.save(w);}
 private OurWork find(Long id){return repository.findById(id).filter(w->!Boolean.TRUE.equals(w.getDelFlg())).orElseThrow(()->new RuntimeException("Our Work item not found"));}
}
