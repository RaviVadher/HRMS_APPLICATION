import { useState, useEffect } from "react";
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
  const [pageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);

  const [showCreateForm, setShowCreateForm] = useState(false);

  // search states
  const [searchQuery, setSearchQuery] = useState("");
  const [tagFilter, setTagFilter] = useState("");
  const [authorFilter, setAuthorFilter] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  const fallbackAvatar = (size = 40) =>
    `data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='${size}' height='${size}'><rect width='100%' height='100%' fill='%23e5e7eb'/><text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' font-size='${Math.floor(
      size / 2.2
    )}' fill='%236b7280'>U</text></svg>`;

  const fetchAchievements = async () => {
    try {
      setLoading(true);
      setError(null);

      let response;

      const hasFilters =
        searchQuery || tagFilter || authorFilter || startDate || endDate;

      if (hasFilters) {
        response = await api.get("/achievements/search", {
          params: {
            query: searchQuery || undefined,
            tag: tagFilter || undefined,
            author: authorFilter || undefined,
            startDate: startDate || undefined,
            endDate: endDate || undefined,
          },
        });

        const posts = response.data?.data || response.data || [];
        setAchievements(posts);
        setTotalPages(1);
      } else {
        response = await api.get("/achievements", {
          params: { pageNumber, pageSize },
        });

        const responseData = response.data?.data || response.data;
        const posts = responseData.content || [];

        setAchievements(posts);
        setTotalPages(responseData.totalPages || 0);
      }
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || "Failed to load achievements");
    } finally {
      setLoading(false);
    }
  };

  // debounce search
  useEffect(() => {
    const delay = setTimeout(() => {
      fetchAchievements();
    }, 400);
    return () => clearTimeout(delay);
  }, [
    pageNumber,
    searchQuery,
    tagFilter,
    authorFilter,
    startDate,
    endDate,
  ]);

  const getAchievementIcon = (postType) => {
    const icons = {
      MILESTONE: "🏆",
      AWARD: "🎖️",
      BADGE: "🏅",
      CERTIFICATE: "📜",
      RECOGNITION: "⭐",
    };
    return icons[postType] ;
  };

  const formatDate = (dateString) =>
    new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });

  const handleCreateAchievementSuccess = (newAchievement) => {
    setAchievements((prev) => [newAchievement, ...prev]);
    setShowCreateForm(false);
  };

  return (
    <DashboardLayout>
      <div className="w-full max-w-6xl mx-auto">

        {/* HEADER */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Achievements</h1>
          <p className="text-gray-600">
            Celebrate milestones across your organization
          </p>
        </div>

        {/* CREATE BUTTON */}
        <div className="mb-6">
          <button
            onClick={() => setShowCreateForm(!showCreateForm)}
            className="px-6 py-2 bg-blue-500 text-white rounded-lg font-medium hover:bg-green-700"
          >
            ✨ Share Achievement
          </button>
        </div>

        {showCreateForm && (
          <CreateAchievementForm
            onSuccess={handleCreateAchievementSuccess}
            onCancel={() => setShowCreateForm(false)}
          />
        )}

        {/* SEARCH FILTERS */}
        <div className="bg-white p-4 rounded-lg shadow mb-6 grid md:grid-cols-6 gap-3">

          <input
            type="text"
            placeholder="Search..."
            value={searchQuery}
            onChange={(e) => {
              setPageNumber(0);
              setSearchQuery(e.target.value);
            }}
            className="border p-2 rounded"
          />

          <input
            type="text"
            placeholder="Tag"
            value={tagFilter}
            onChange={(e) => setTagFilter(e.target.value)}
            className="border p-2 rounded"
          />

          <input
            type="text"
            placeholder="Author"
            value={authorFilter}
            onChange={(e) => setAuthorFilter(e.target.value)}
            className="border p-2 rounded"
          />

          <input
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
            className="border p-2 rounded"
          />

          <input
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            className="border p-2 rounded"
          />

          <button
            onClick={() => {
              setSearchQuery("");
              setTagFilter("");
              setAuthorFilter("");
              setStartDate("");
              setEndDate("");
              setPageNumber(0);
            }}
            className="bg-gray-200 rounded font-medium"
          >
            Clear
          </button>
        </div>

        {/* PAGINATION */}
        <div className="flex gap-3 mb-6">
          <button
            disabled={loading || pageNumber === 0}
            onClick={() => setPageNumber((p) => p - 1)}
            className="px-4 py-2 bg-blue-600 text-white rounded disabled:bg-gray-300"
          >
            ← Previous
          </button>

          <button
            disabled={loading || pageNumber >= totalPages - 1}
            onClick={() => setPageNumber((p) => p + 1)}
            className="px-4 py-2 bg-blue-600 text-white rounded disabled:bg-gray-300"
          >
            Next →
          </button>

          <span className="px-4 py-2">
            Page {pageNumber + 1} of {Math.max(1, totalPages)}
          </span>
        </div>

        {/* LOADING */}
        {loading && (
          <div className="text-center py-10">Loading achievements...</div>
        )}

        {/* ERROR */}
        {error && !loading && (
          <div className="bg-red-100 text-red-700 p-4 rounded mb-6">
            {error}
          </div>
        )}

        {/* EMPTY */}
        {!loading && achievements.length === 0 && (
          <div className="text-center py-16">
            <div className="text-5xl">🎯</div>
            <p>No achievements found</p>
          </div>
        )}

        {/* LIST */}
        <div className="space-y-4">
          {achievements.map((post) => (
            <div
              key={post.postId}
              className="bg-white rounded-lg shadow p-6 border-l-4 border-blue-600"
            >
              <div className="flex gap-4">

                <div className="text-4xl">
                  {getAchievementIcon(post.postType)}
                </div>

                <div className="flex-1">

                  <Link
                    to={`/achievements/${post.postId}`}
                    className="font-semibold text-lg hover:underline"
                  >
                    {post.title}
                  </Link>

                  <p className="text-gray-600 text-sm mt-1">
                    {post.description}
                  </p>

                 

                  {/* AUTHOR */}
                  <div className="flex items-center gap-2 mt-3">
                    <img
                      src={post.author?.profilePicture || fallbackAvatar(32)}
                      className="w-8 h-8 rounded-full"
                    />
                    <span className="text-sm">
                     {post.isSystemGenerated ? "HRMS" : post.author?.name} 
                    </span>
                  </div>

                  {/* TAGS */}
                  {post.tags && (
                    <div className="flex gap-2 mt-2">
                      {post.tags.split(",").map((tag) => (
                        <span
                          key={tag}
                          className="px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded-full"
                        >
                          {tag.trim()}
                        </span>
                      ))}
                    </div>
                  )}
                </div>

                {/* RIGHT */}
                <div className="text-right text-sm text-gray-500">
                  {formatDate(post.createdAt)}

                  <div className="mt-3">
                    ❤️ {post.likeCount || 0} <br />
                    💬 {post.commentCount || 0}
                  </div>
                </div>

              </div>
            </div>
          ))}
        </div>

      </div>
    </DashboardLayout>
  );
};

export default AchievementsFeed;