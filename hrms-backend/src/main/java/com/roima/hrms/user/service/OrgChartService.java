package com.roima.hrms.user.service;

import com.roima.hrms.user.dto.OrgChartDto;
import com.roima.hrms.user.dto.UserDto;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrgChartService {

     private final UserRepository userRepository;

     public OrgChartService(UserRepository userRepository) {
         this.userRepository = userRepository;
     }
     public OrgChartDto getOrgChart(Long id) {

         User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

         return OrgChartDto.builder()
                 .user(toDto(u))
                 .managerChain(getManager(u))
                 .directReports(getReports(u.getId()))
                 .build();

     }


     private List<UserDto>  getManager(User u ){
           List<UserDto> list = new ArrayList<>();
         Set<Long> visited = new HashSet<>();

           while(u.getManager()!=null){
                u=u.getManager();

                if(visited.contains(u.getId())){
                    break;
                }
                visited.add(u.getId());
                list.add(toDto(u));
           }

           Collections.reverse(list);
           return list;
     }

     private List<UserDto> getReports(Long id){

         return userRepository.findByManagerId(id)
                 .stream()
                 .map(this::toDto)
                 .toList();
     }



     public List<UserDto> search(String name)
     {
         return userRepository.findByNameContainingIgnoreCase(name)
                 .stream()
                 .map(this::toDto)
                 .toList();
     }

    public List<UserDto> getRoots()
    {
        return userRepository.findByManager_IdIsNull()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private UserDto toDto(User u) {
        return  UserDto.builder()
                .id(u.getId())
                .name(u.getName())
                .designation(u.getDesignation())
                .build();
    }
}
