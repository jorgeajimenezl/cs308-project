package com.harbourspace.cs308.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.harbourspace.cs308.dto.PollDto;
import com.harbourspace.cs308.model.Poll;
import com.harbourspace.cs308.model.PollOption;

@Mapper(componentModel = "spring")
public interface PollMapper {
    @Mapping(target = "options", expression = "java(mapStringsToPollOptions(pollDto.getOptions()))")
    Poll toPoll(PollDto pollDto);
    
    @Mapping(target = "options", expression = "java(mapOptionsToStrings(poll.getOptions()))")
    PollDto toPollDto(Poll poll);
    
    default List<String> mapOptionsToStrings(List<PollOption> options) {
        if (options == null) return null;
        return options.stream().map(PollOption::getOption).collect(Collectors.toList());
    }
    
    default List<PollOption> mapStringsToPollOptions(List<String> optionTexts) {
        if (optionTexts == null) return null;
        return optionTexts.stream().map(text -> {
            PollOption option = new PollOption();
            option.setOption(text);
            return option;
        }).collect(Collectors.toList());
    }
}