package com.rhymthwave.ServiceAdmin.Implement;

import com.rhymthwave.DAO.TagDAO;
import com.rhymthwave.ServiceAdmin.ITagsService;
import com.rhymthwave.Utilities.GetCurrentTime;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImp implements ITagsService {

    private final TagDAO tagDAO;
    private  final GetHostByRequest getIdByRequest;
    @Override
    public Tag create(Tag entity) {
        return tagDAO.save(entity);
    }

    @Override
    public Tag update(Tag entity) {
        return tagDAO.save(entity);
    }

    @Override
    public Boolean delete(Long key) {
        Tag tag = findOne(key);
        if(tag != null){
            tagDAO.delete(tag);
            return true;
        }
        return false;
    }

    @Override
    public Tag findOne(Long key) {
        return tagDAO.findById(Math.toIntExact(key)).orElse(null);
    }

    @Override
    public List<Tag> findAll() {
        return tagDAO.findAll();
    }

    @Override
    public Tag save(Tag tag, HttpServletRequest request) {
        if (tag.getCreateBy() == null) {
            tag.setCreateBy(getIdByRequest.getEmailByRequest(request));
            tag.setCreateDate(GetCurrentTime.getTimeNow());
        }
        tag.setModifiedBy(getIdByRequest.getEmailByRequest(request));
        tag.setModifiDate(GetCurrentTime.getTimeNow());
        create(tag);

        return tag;
    }
}
