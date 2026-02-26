import api from "../api/axios";

const ACHIEVEMENTS_API_BASE = "/achievements";

/**
 * Fetch all achievements with pagination
 * @param {number} pageNumber - Page number (0-indexed)
 * @param {number} pageSize - Items per page
 * @param {string} sortBy - Sort field (default: createdAt)
 * @returns {Promise} Paginated achievement list with PaginatedResponse wrapper
 */
export const fetchAchievements = async (pageNumber = 0, pageSize = 10, sortBy = "createdAt") => {
  try {
    // Backend returns PaginatedResponse<AchievementPostDTO>
    const response = await api.get(`${ACHIEVEMENTS_API_BASE}/feed`, {
      params: { 
        pageNumber,
        pageSize,
        sortBy,
      },
    });
    
    // Response structure: { success, message, data: { content, pageNumber, pageSize, totalElements, totalPages, isFirstPage, isLastPage, hasNextPage, hasPreviousPage } }
    console.log("Fetched achievements:", response.data);
    return response.data?.data || response.data; // Return paginated data
  } catch (error) {
    console.error("Error fetching achievements:", error);
    throw error;
  }
};

/**
 * Fetch a single achievement by ID
 * @param {string} id - Achievement/Post ID
 * @returns {Promise} Achievement details (AchievementPostDTO)
 */
export const fetchAchievementById = async (id) => {
  try {
    const response = await api.get(`${ACHIEVEMENTS_API_BASE}/${id}`);
    // Response structure: { success, message, data: AchievementPostDTO }
    return response.data?.data || response.data;
  } catch (error) {
    console.error("Error fetching achievement:", error);
    throw error;
  }
};

/**
 * Fetch achievements for a specific employee
 * @param {string} employeeId - Employee ID
 * @returns {Promise} Employee achievements
 */
export const fetchEmployeeAchievements = async (employeeId) => {
  try {
    const response = await api.get(
      `${ACHIEVEMENTS_API_BASE}/employee/${employeeId}`
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching employee achievements:", error);
    throw error;
  }
};

/**
 * Create a new achievement post
 * @param {FormData} formData - FormData with title, description, tags, visibility, mediaFile
 * @returns {Promise} Created achievement (AchievementPostDTO)
 */
export const createAchievement = async (formData) => {
  try {
    // FormData for multipart upload with optional mediaFile
    const response = await api.post(`${ACHIEVEMENTS_API_BASE}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    // Response structure: { success, message, data: AchievementPostDTO }
    return response.data?.data || response.data;
  } catch (error) {
    console.error("Error creating achievement:", error);
    throw error;
  }
};

/**
 * Update an achievement
 * @param {string} id - Achievement ID
 * @param {Object} updateData - Updated achievement data
 * @returns {Promise} Updated achievement
 */
export const updateAchievement = async (id, updateData) => {
  try {
    const response = await api.put(
      `${ACHIEVEMENTS_API_BASE}/${id}`,
      updateData
    );
    return response.data;
  } catch (error) {
    console.error("Error updating achievement:", error);
    throw error;
  }
};

/**
 * Delete an achievement
 * @param {string} id - Achievement ID
 * @returns {Promise} Response
 */
export const deleteAchievement = async (id) => {
  try {
    const response = await api.delete(`${ACHIEVEMENTS_API_BASE}/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error deleting achievement:", error);
    throw error;
  }
};

/**
 * Like/Unlike an achievement
 * @param {string} id - Achievement ID
 * @returns {Promise} Updated achievement
 */

export const toggleAchievementLike = async (postId, isLiked) => {
  if (isLiked) {
    const response = await api.delete(`${ACHIEVEMENTS_API_BASE}/${postId}/like`);
    return response.data;
  } else {
   const response = await api.post(`${ACHIEVEMENTS_API_BASE}/${postId}/like`);
    return response.data;
  }
};

/**
 * Share an achievement
 * @param {string} id - Achievement ID
 * @param {Object} shareData - Share details
 * @returns {Promise} Shared achievement
 */
export const shareAchievement = async (id, shareData) => {
  try {
    const response = await api.post(
      `${ACHIEVEMENTS_API_BASE}/${id}/share`,
      shareData
    );
    return response.data;
  } catch (error) {
    console.error("Error sharing achievement:", error);
    throw error;
  }
};

/**
 * Get achievement statistics
 * @returns {Promise} Statistics data
 */
export const fetchAchievementStats = async () => {
  try {
    const response = await api.get(`${ACHIEVEMENTS_API_BASE}/stats`);
    return response.data;
  } catch (error) {
    console.error("Error fetching achievement stats:", error);
    throw error;
  }
};

/**
 * Comments API
 */
export const fetchComments = async (postId) => {
  try {
    const response = await api.get(`${ACHIEVEMENTS_API_BASE}/${postId}/comments`);
    // Expecting { success, message, data: [comments] }
    return response.data?.data || response.data;
  } catch (error) {
    console.error("Error fetching comments:", error);
    throw error;
  }
};

//add comment
export const addComment = async (postId, text) => {
  try {
    const response = await api.post(`${ACHIEVEMENTS_API_BASE}/${postId}/comments`, { text });
    return response.data?.data || response.data;
  } catch (error) {
    console.error("Error adding comment:", error);
    throw error;
  }
};

//update comment
export const updateComment = async (postId, commentId, text) => {
  try {
    const response = await api.put(`${ACHIEVEMENTS_API_BASE}/${postId}/comments/${commentId}`, { text });
    return response.data?.data || response.data;
  } catch (error) {
    console.error("Error updating comment:", error);
    throw error;
  }
};

//delete comment
export const deleteComment = async (postId, commentId) => {
  try {
    const response = await api.delete(`${ACHIEVEMENTS_API_BASE}/${postId}/comments/${commentId}`);
    return response.data;
  } catch (error) {
    console.error("Error deleting comment:", error);
    throw error;
  }
};

export const deleteCommentHr = async (postId, commentId) => {
  try {
    const response = await api.delete(`${ACHIEVEMENTS_API_BASE}/${postId}/comments/${commentId}/moderate`);
    return response.data;
  } catch (error) {
    console.error("Error deleting comment:", error);
    throw error;
  }
};

/**
 * Search achievements
 * @param {string} query - Search query
 * @returns {Promise} Search results
 */
export const searchAchievements = async (query) => {
  try {
    const response = await api.get(`${ACHIEVEMENTS_API_BASE}/search`, {
      params: { q: query },
    });
    return response.data;
  } catch (error) {
    console.error("Error searching achievements:", error);
    throw error;
  }
};

//delete post by Hr
export const moderateDeletePost = async (postId, reason = "") => {
  return api.delete(`${ACHIEVEMENTS_API_BASE}/${postId}/moderate`, {
    params: { reason },
  });
};

//delete post by author
export const deletePost = async (postId) => {
  return api.delete(`${ACHIEVEMENTS_API_BASE}/${postId}`);
};

//update post by author
export const updatePost = async (postId, data) => {
  return api.put(`${ACHIEVEMENTS_API_BASE}/${postId}`, data);
};

//media preview
export const previewMedia = async (postId, mediaId) => {
  return api.get(
    `${ACHIEVEMENTS_API_BASE}/${postId}/media/${mediaId}/preview`,
    { responseType: "blob" }
  );
};

/* ================= COMMENT LIKE ================= */

export const likeCommentHandle = async (commentId,isLiked) => {
 
  if(!isLiked)
  {
       await api.post(`${ACHIEVEMENTS_API_BASE}/comment/${commentId}/like`);
  }
  else{
         await api.delete(`${ACHIEVEMENTS_API_BASE}/comment/${commentId}/like`);
  }
  
};



export const getCommentLikeCount = async (commentId) => {
  const res = await api.get(`${ACHIEVEMENTS_API_BASE}/comment/${commentId}/like/count`);
  return res.data;
};

