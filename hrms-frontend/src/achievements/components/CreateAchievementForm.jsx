import React, { useState, useContext } from "react";
import { createAchievement } from "../achievementsAPI";
import { useAuth } from "../../context/AuthContext";

const CreateAchievementForm = ({ onSuccess, onCancel }) => {
  const { user } = useAuth();

  const [formData, setFormData] = useState({
    title: "",
    description: "",
    tags: "",
    visibility: "ALL_EMPLOYEES",
    mediaFile: null,
  });
  const [mediaPreview, setMediaPreview] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState("");

  const visibilityOptions = ["ALL_EMPLOYEES", "DEPARTMENT_ONLY"];


  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    setError(null);
  };

  const handleMediaChange = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      setFormData((prev) => ({
        ...prev,
        mediaFile: file,
      }));

      // Create preview for images
      if (file.type.startsWith("image/")) {
        const reader = new FileReader();
        reader.onload = (event) => {
          setMediaPreview({
            name: file.name,
            type: file.type,
            size: file.size,
            url: event.target?.result,
          });
        };
        reader.readAsDataURL(file);
      } else {
        // For videos, just show name
        setMediaPreview({
          name: file.name,
          type: file.type,
          size: file.size,
          url: null,
        });
      }
    }
    setError(null);
  };

  const removeMedia = () => {
    setFormData((prev) => ({
      ...prev,
      mediaFile: null,
    }));
    setMediaPreview(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccessMessage("");

    // Validation
    if (!formData.title.trim()) {
      setError("Title is required");
      return;
    }

    if (formData.title.trim().length < 3) {
      setError("Title must be at least 3 characters");
      return;
    }

    if (formData.title.trim().length > 255) {
      setError("Title must not exceed 255 characters");
      return;
    }

    if (!formData.description.trim()) {
      setError("Description is required");
      return;
    }

    if (formData.description.trim().length < 10) {
      setError("Description must be at least 10 characters");
      return;
    }

    if (formData.description.trim().length > 5000) {
      setError("Description must not exceed 5000 characters");
      return;
    }

    if (formData.tags.length > 500) {
      setError("Tags must not exceed 500 characters");
      return;
    }

    if (!formData.visibility) {
      setError("Visibility must be specified");
      return;
    }

    // Require a single media file (image or video) as per backend contract
    if (!formData.mediaFile) {
      setError("Please attach an image or video file.");
      return;
    }

    if (!formData.mediaFile.type.startsWith("image/") && !formData.mediaFile.type.startsWith("video/")) {
      setError("Only image or video files are allowed.");
      return;
    }

    setLoading(true);

    try {
      // Use FormData for multipart/form-data with optional media file
      const payload = new FormData();
      payload.append("title", formData.title.trim());
      payload.append("description", formData.description.trim());
      payload.append("tags", formData.tags.trim());
      payload.append("visibility", formData.visibility);

      // Append media file (required)
      payload.append("mediaFile", formData.mediaFile);

      const response = await createAchievement(payload);

      setSuccessMessage("Post created successfully! 🎉");

      // Reset form
      setFormData({
        title: "",
        description: "",
        tags: "",
        visibility: "PUBLIC",
        mediaFile: null,
      });
      setMediaPreview(null);

      // Call success callback after 2 seconds
      setTimeout(() => {
        if (onSuccess) onSuccess(response);
      }, 2000);
    } catch (err) {
      console.error("Error creating post:", err);
      setError(
        err.response?.data?.message ||
          err.message ||
          "Failed to create post. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-lg p-6 mb-6">
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-gray-900">Share an Achievement</h2>
        <p className="text-gray-600 text-sm mt-1">
          Celebrate milestones and recognitions across your organization
        </p>
      </div>

      <form onSubmit={handleSubmit}>
        {/* Error Message */}
        {error && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
            <div className="flex items-center">
              <span className="text-red-600 mr-2">⚠️</span>
              <p className="text-red-700 text-sm font-medium">{error}</p>
            </div>
          </div>
        )}

        {/* Success Message */}
        {successMessage && (
          <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg">
            <div className="flex items-center">
              <span className="text-green-600 mr-2">✅</span>
              <p className="text-green-700 text-sm font-medium">{successMessage}</p>
            </div>
          </div>
        )}

        {/* Title Input */}
        <div className="mb-4">
          <label className="block text-sm font-semibold text-gray-900 mb-2">
            Title * (3-255 characters)
          </label>
          <input
            type="text"
            name="title"
            value={formData.title}
            onChange={handleInputChange}
            placeholder="e.g., Milestone Achieved, Employee Recognition"
            maxLength="255"
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all"
            disabled={loading}
          />
          <p className="text-xs text-gray-500 mt-1">
            {formData.title.length}/255 characters
          </p>
        </div>

        {/* Description Input */}
        <div className="mb-4">
          <label className="block text-sm font-semibold text-gray-900 mb-2">
            Description * (10-5000 characters)
          </label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleInputChange}
            placeholder="Provide details about the achievement..."
            maxLength="5000"
            rows="5"
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all resize-none"
            disabled={loading}
          />
          <p className="text-xs text-gray-500 mt-1">
            {formData.description.length}/5000 characters
          </p>
        </div>

        {/* Tags Input */}
        <div className="mb-4">
          <label className="block text-sm font-semibold text-gray-900 mb-2">
            Tags (Optional, max 500 characters)
          </label>
          <input
            type="text"
            name="tags"
            value={formData.tags}
            onChange={handleInputChange}
            placeholder="e.g., performance, innovation, teamwork (comma-separated)"
            maxLength="500"
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all"
            disabled={loading}
          />
          <p className="text-xs text-gray-500 mt-1">
            {formData.tags.length}/500 characters
          </p>
        </div>

        {/* Visibility Selection */}
        <div className="mb-6">
          <label className="block text-sm font-semibold text-gray-900 mb-2">
            Visibility *
          </label>
          <div className="flex flex-wrap gap-2">
            {visibilityOptions.map((option) => (
              <button
                key={option}
                type="button"
                onClick={() =>
                  setFormData((prev) => ({ ...prev, visibility: option }))
                }
                disabled={loading}
                className={`px-4 py-2 rounded-lg font-medium transition-all ${
                  formData.visibility === option
                    ? "bg-blue-600 text-white shadow-md"
                    : "bg-gray-100 text-gray-700 hover:bg-gray-200"
                } disabled:opacity-50`}
              >
                {option === "ALL_EMPLOYEES" && "🌐 "}
                {option === "DEPARTMENT_ONLY" && "🔒 "}
                {option.replace(/_/g, " ")}
              </button>
            ))}
          </div>
        </div>

        {/* Media Upload Section */}
        <div className="mb-6">
          <label className="block text-sm font-semibold text-gray-900 mb-2">
            Media (Optional - Image or Video)
          </label>
          <div className="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center hover:border-blue-500 transition-colors">
            <input
              type="file"
              onChange={handleMediaChange}
              disabled={loading}
              className="hidden"
              id="media-input"
              accept="image/*,video/*"
            />
            <label htmlFor="media-input" className="cursor-pointer">
              <div className="text-4xl mb-2">📸</div>
              <p className="text-gray-700 font-medium">Click to upload or drag and drop</p>
              <p className="text-xs text-gray-500 mt-1">
                PNG, JPG, GIF, MP4, WebM (up to 50MB)
              </p>
            </label>
          </div>

          {/* Media Preview */}
          {mediaPreview && (
            <div className="mt-4">
              <h4 className="text-sm font-semibold text-gray-900 mb-2">
                Media Preview
              </h4>
              <div className="bg-gray-50 p-4 rounded-lg border border-gray-200">
                <div className="flex items-start gap-3">
                  <div className="flex-shrink-0">
                    {mediaPreview.type.startsWith("image/") && mediaPreview.url ? (
                      <img
                        src={mediaPreview.url}
                        alt="preview"
                        className="w-20 h-20 object-cover rounded"
                      />
                    ) : (
                      <div className="w-20 h-20 bg-gray-300 rounded flex items-center justify-center text-2xl">
                        🎥
                      </div>
                    )}
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-gray-900 truncate">
                      {mediaPreview.name}
                    </p>
                    <p className="text-xs text-gray-500">
                      {(mediaPreview.size / 1024 / 1024).toFixed(2)} MB
                    </p>
                  </div>
                  <button
                    type="button"
                    onClick={removeMedia}
                    disabled={loading}
                    className="text-red-600 hover:text-red-800 disabled:opacity-50 flex-shrink-0"
                  >
                    ✕
                  </button>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Form Actions */}
        <div className="flex gap-3 justify-end">
          <button
            type="button"
            onClick={onCancel}
            disabled={loading}
            className="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg font-medium hover:bg-gray-50 transition-colors disabled:opacity-50"
          >
            Cancel
          </button>
          <button
            type="submit"
            disabled={loading}
            className="px-6 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-colors disabled:bg-gray-400 flex items-center gap-2"
          >
            {loading ? (
              <>
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                Creating...
              </>
            ) : (
              "Create Post"
            )}
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateAchievementForm;
