package gr.aueb.cf9.servicehub.service;

import gr.aueb.cf9.servicehub.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf9.servicehub.core.exceptions.EntityNotFoundException;
import gr.aueb.cf9.servicehub.dto.ProfessionalProfileRequestDTO;
import gr.aueb.cf9.servicehub.dto.ProfessionalProfileResponseDTO;
import gr.aueb.cf9.servicehub.mapper.Mapper;
import gr.aueb.cf9.servicehub.model.entity.ProfessionalProfile;
import gr.aueb.cf9.servicehub.model.entity.ServiceCategory;
import gr.aueb.cf9.servicehub.model.entity.User;
import gr.aueb.cf9.servicehub.repository.ProfessionalProfileRepository;
import gr.aueb.cf9.servicehub.repository.ServiceCategoryRepository;
import gr.aueb.cf9.servicehub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfessionalProfileService {

    private final ProfessionalProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final Mapper mapper;

    public ProfessionalProfileService(ProfessionalProfileRepository profileRepository,
                                      UserRepository userRepository,
                                      ServiceCategoryRepository categoryRepository,
                                      Mapper mapper) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public ProfessionalProfileResponseDTO createProfile(String userEmail, ProfessionalProfileRequestDTO dto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userEmail));

        if (profileRepository.findByUserId(user.getId()).isPresent()) {
            throw new EntityAlreadyExistsException("Professional profile already exists for this user");
        }

        ProfessionalProfile profile = new ProfessionalProfile();
        profile.setBusinessName(dto.businessName());
        profile.setDescription(dto.description());
        profile.setLocation(dto.location());
        profile.setPhone(dto.phone());
        profile.setExperienceYears(dto.experienceYears());
        profile.setUser(user);
        profile.setCategories(resolveCategories(dto.categoryIds()));

        ProfessionalProfile saved = profileRepository.save(profile);
        return mapper.toResponseDTO(saved);
    }

    public ProfessionalProfileResponseDTO getMyProfile(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userEmail));

        ProfessionalProfile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Professional profile not found for this user"));

        return mapper.toResponseDTO(profile);
    }

    public ProfessionalProfileResponseDTO updateProfile(String userEmail, ProfessionalProfileRequestDTO dto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userEmail));

        ProfessionalProfile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Professional profile not found for this user"));

        profile.setBusinessName(dto.businessName());
        profile.setDescription(dto.description());
        profile.setLocation(dto.location());
        profile.setPhone(dto.phone());
        profile.setExperienceYears(dto.experienceYears());
        profile.setCategories(resolveCategories(dto.categoryIds()));

        ProfessionalProfile saved = profileRepository.save(profile);
        return mapper.toResponseDTO(saved);
    }

    private Set<ServiceCategory> resolveCategories(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Set.of();
        }
        return new java.util.HashSet<>(categoryRepository.findAllById(categoryIds));
    }
}