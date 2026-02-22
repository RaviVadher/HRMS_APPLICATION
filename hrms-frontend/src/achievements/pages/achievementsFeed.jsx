import React, { useState, useEffect } from "react";
import { useContext } from "react";
import api from "../../api/axios";
import { Link } from "react-router-dom";
import DashboardLayout from "../../layout/DashboardLayout";
import { useAuth } from "../../context/AuthContext";
import CreateAchievementForm from "../components/CreateAchievementForm";

const AchievementsFeed = () => {
  const { user } = useAuth();
  const [achievements, setAchievements] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [showCreateForm, setShowCreateForm] = useState(false);

  const fallbackAvatar = (size = 40) => `data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='${size}' height='${size}'><rect width='100%' height='100%' fill='%23e5e7eb'/><text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' font-size='${Math.floor(size/2.2)}' fill='%236b7280'>U</text></svg>`;

  useEffect(() => {
    const fetchAchievements = async () => {
      try {
        setLoading(true);
        setError(null);

        
        const paginatedData = await api.get("/achievements", {
          params: {
            pageNumber: pageNumber,
            pageSize: pageSize,
          },
        });

        // Backend returns structure: { success, message, data: PaginatedResponse }
        const responseData = paginatedData.data?.data || paginatedData.data;
        
        // Extract posts from content array
        const posts = responseData.content || [];
        setAchievements(posts);
        setTotalPages(responseData.totalPages || 0);
        
        console.log("Posts loaded:", posts);
      } catch (err) {
        console.error("Error fetching achievements:", err);
        setError(
          err.response?.data?.message || "Failed to load achievements"
        );
      } finally {
        setLoading(false);
      }
    };

    fetchAchievements();
  }, [pageNumber, pageSize]);

  const getAchievementIcon = (postType) => {
    const icons = {
      MILESTONE: "🏆",
      AWARD: "🎖️",
      BADGE: "🏅",
      CERTIFICATE: "📜",
      RECOGNITION: "⭐",
      POST: "✨",
      default: "✨",
    };
    return icons[postType] || icons.default;
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("en-US", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  const handleCreateAchievementSuccess = (newAchievement) => {
    // Add new achievement to the top of the list
    setAchievements((prev) => [newAchievement, ...prev]);
    setShowCreateForm(false);
  };

  return (
    <DashboardLayout>
      <div className="w-full max-w-6xl mx-auto">
        {/* Header Section */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Achievements</h1>
          <p className="text-gray-600">
            Celebrate milestones and achievements across your organization
          </p>
        </div>

        {/* Create Achievement Button */}
        <div className="mb-6">
          <button
            onClick={() => setShowCreateForm(!showCreateForm)}
            className="px-6 py-2 bg-green-600 text-white rounded-lg font-medium hover:bg-green-700 transition-colors flex items-center gap-2"
          >
            <span>✨</span>
            Share an Achievement
          </button>
        </div>

        {/* Create Achievement Form */}
        {showCreateForm && (
          <CreateAchievementForm
            onSuccess={handleCreateAchievementSuccess}
            onCancel={() => setShowCreateForm(false)}
          />
        )}

        {/* Filter Buttons */}
        <div className="flex gap-3 mb-6">
          {["previous", "next"].map((btn) => (
            <button
              key={btn}
              onClick={() => {
                if (btn === "previous" && pageNumber > 0) {
                  setPageNumber(pageNumber - 1);
                } else if (btn === "next" && pageNumber < totalPages - 1) {
                  setPageNumber(pageNumber + 1);
                }
              }}
              disabled={
                loading ||
                (btn === "previous" && pageNumber === 0) ||
                (btn === "next" && pageNumber >= totalPages - 1)
              }
              className={`px-4 py-2 rounded-lg font-medium transition-all ${
                loading || (btn === "previous" && pageNumber === 0) || (btn === "next" && pageNumber >= totalPages - 1)
                  ? "bg-gray-200 text-gray-500 cursor-not-allowed"
                  : "bg-blue-600 text-white hover:bg-blue-700"
              }`}
            >
              {btn === "previous" ? "← Previous" : "Next →"}
            </button>
          ))}
          <span className="px-4 py-2 text-gray-700 font-medium">
            Page {pageNumber + 1} of {Math.max(1, totalPages)}
          </span>
        </div>

        {/* Loading State */}
        {loading && (
          <div className="flex flex-col items-center justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>
            <p className="text-gray-600">Loading achievements...</p>
          </div>
        )}

        {/* Error State */}
        {error && !loading && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
            <div className="flex items-center">
              <span className="text-red-600 text-lg mr-3">⚠️</span>
              <div>
                <h3 className="font-semibold text-red-900">Error Loading Achievements</h3>
                <p className="text-red-700">{error}</p>
              </div>
            </div>
          </div>
        )}

        {/* Empty State */}
        {!loading && (!achievements || achievements.length === 0) && (
          <div className="bg-white rounded-lg shadow p-12 text-center">
            <div className="text-6xl mb-4">🎯</div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">
              No Achievements Yet
            </h3>
            <p className="text-gray-600">
              There are no achievements to display. Keep up the great work!
            </p>
          </div>
        )}

        {/* Achievements Feed */}
        {!loading && achievements && achievements.length > 0 && (
          <div className="space-y-4">
            {achievements.map((post) => (
              <div
                key={post.postId}
                className="bg-white rounded-lg shadow hover:shadow-md transition-shadow p-6 border-l-4 border-blue-600"
              >
                <div className="flex items-start justify-between">
                  <div className="flex items-start gap-4 flex-1">
                    {/* Post Icon */}
                    <div className="text-4xl">
                      {getAchievementIcon(post.postType)}
                    </div>

                    {/* Achievement Details */}
                    <div className="flex-1">
                      <div className="font-semibold text-lg text-gray-900">
                        <Link to={`/achievements/${post.postId}`} className="hover:underline">
                          {post.title}
                        </Link>
                      </div>
                      <p className="text-gray-600 text-sm mt-1">
                        {post.description}
                      </p>

                      {/* Media (image or video) - backend returns media: PostMediaDTO[] */}
                      {post.media && post.media.length > 0 && (() => {
                        const m = post.media[0] || {};
                        const src = m.url || m.mediaUrl || m.fileUrl || m.path || m.src;
                        const type = m.mediaType || m.mimeType || m.type || "";
                        if (!src) return null;
                        return (
                          <div className="mt-3">
                            {type && type.startsWith("image") ? (
                              <img src={src} alt={post.title} className="w-full max-h-72 object-cover rounded" />
                            ) : (
                              <video controls className="w-full max-h-72 rounded">
                                <source src={src} type={type || "video/mp4"} />
                                Your browser does not support the video tag.
                              </video>
                            )}
                          </div>
                        );
                      })()}

                      {/* Author Info */}
                      <div className="flex items-center gap-4 mt-3">
                        <div className="flex items-center gap-2">
                          <img
                            src={post.author?.profilePicture || fallbackAvatar(32)}
                            alt={post.author?.name}
                            className="w-8 h-8 rounded-full bg-gray-200 object-cover"
                            onError={(e) => {
                              e.currentTarget.onerror = null;
                              e.currentTarget.src = fallbackAvatar(32);
                            }}
                          />
                          <span className="text-sm text-gray-700">
                            {post.author?.name} {post.author?.lastName}
                          </span>
                        </div>
                        <span className="text-xs text-gray-500">
                          {post.author?.department && `• ${post.author.department}`}
                        </span>
                      </div>

                      {/* Tags */}
                      {post.tags && (
                        <div className="flex gap-2 mt-3">
                          {post.tags.split(",").map((tag) => (
                            <span
                              key={tag.trim()}
                              className="inline-block px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded-full"
                            >
                              {tag.trim()}
                            </span>
                          ))}
                        </div>
                      )}
                    </div>
                  </div>

                  {/* Date and Actions */}
                  <div className="text-right">
                    <div className="text-xs text-gray-500">
                      {formatDate(post.createdAt)}
                    </div>

                    {/* Engagement Stats */}
                    {(post.likeCount || post.commentCount) && (
                      <div className="mt-3 text-sm text-gray-600">
                        <button className="hover:text-blue-600 transition-colors mr-3">
                          ❤️ {post.likeCount || 0}
                        </button>
                        <button className="hover:text-blue-600 transition-colors">
                          💬 {post.commentCount || 0}
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </DashboardLayout>
  );
};

export default AchievementsFeed;
