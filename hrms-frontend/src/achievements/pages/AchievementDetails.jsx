import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import DashboardLayout from "../../layout/DashboardLayout";
import {
  toggleAchievementLike,
  shareAchievement,
  fetchComments,
  addComment,
  updateComment,
  deleteComment,
} from "../achievementsAPI";
import { useAuth } from "../../context/AuthContext";
import api from "../../api/axios";

const AchievementDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const fallbackAvatar = (size = 40) => `data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='${size}' height='${size}'><rect width='100%' height='100%' fill='%23e5e7eb'/><text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' font-size='${Math.floor(size/2.2)}' fill='%236b7280'>U</text></svg>`;

  const [achievement, setAchievement] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [comments, setComments] = useState([]);
  const [commentsLoading, setCommentsLoading] = useState(true);
  const [newComment, setNewComment] = useState("");
  const [editingCommentId, setEditingCommentId] = useState(null);
  const [editingText, setEditingText] = useState("");

  useEffect(() => {
    if (!id) return;

    const load = async () => {
      try {
        setLoading(true);
        const resp = await api.get(`/achievements/${id}`);
        const data = resp.data?.data || resp.data;
        setAchievement(data);
      } catch (err) {
        console.error("Error loading achievement:", err);
        setError(err.response?.data?.message || err.message || "Failed to load achievement");
      } finally {
        setLoading(false);
      }
    };

    const loadComments = async () => {
      try {
        setCommentsLoading(true);
        const data = await fetchComments(id);
        setComments(Array.isArray(data) ? data : data?.content || []);
      } catch (err) {
        console.error("Failed to load comments:", err);
      } finally {
        setCommentsLoading(false);
      }
    };

    load();
    loadComments();
  }, [id]);

  const handleLike = async () => {
    if (!achievement) return;
    try {
      await toggleAchievementLike(achievement.postId || achievement.id);
      setAchievement((prev) => ({
        ...prev,
        isLikedByCurrentUser: !prev.isLikedByCurrentUser,
        likeCount: (prev.likeCount || 0) + (prev.isLikedByCurrentUser ? -1 : 1),
      }));
    } catch (err) {
      console.error("Like failed:", err);
    }
  };

  const handleShare = async () => {
    if (!achievement) return;
    try {
      await shareAchievement(achievement.postId || achievement.id, {});
    } catch (err) {
      console.error("Share failed:", err);
    }
  };

  const handleAddComment = async () => {
    if (!newComment.trim() || !achievement) return;
    try {
      const created = await addComment(achievement.postId || achievement.id, newComment.trim());
      setComments((c) => [created, ...c]);
      setNewComment("");
      setAchievement((prev) => ({ ...prev, commentCount: (prev.commentCount || 0) + 1 }));
    } catch (err) {
      console.error("Add comment failed:", err);
    }
  };

  const startEdit = (comment) => {
    setEditingCommentId(comment.commentId || comment.id);
    setEditingText(comment.text || comment.body || "");
  };

  const cancelEdit = () => {
    setEditingCommentId(null);
    setEditingText("");
  };

  const submitEdit = async (comment) => {
    const cid = comment.commentId || comment.id;
    if (!editingText.trim() || !achievement) return;
    try {
      const updated = await updateComment(achievement.postId || achievement.id, cid, editingText.trim());
      setComments((list) => list.map((c) => ((c.commentId || c.id) === cid ? updated : c)));
      cancelEdit();
    } catch (err) {
      console.error("Update comment failed:", err);
    }
  };

  const handleDeleteComment = async (comment) => {
    const cid = comment.commentId || comment.id;
    if (!achievement) return;
    if (!confirm("Delete this comment?")) return;
    try {
      await deleteComment(achievement.postId || achievement.id, cid);
      setComments((list) => list.filter((c) => (c.commentId || c.id) !== cid));
      setAchievement((prev) => ({ ...prev, commentCount: Math.max(0, (prev.commentCount || 1) - 1) }));
    } catch (err) {
      console.error("Delete comment failed:", err);
    }
  };

  return (
    <DashboardLayout>
      <div className="w-full max-w-3xl mx-auto py-6">
        <button onClick={() => navigate(-1)} className="text-sm text-gray-600 hover:underline mb-4">← Back</button>

        {loading && <div className="py-12 text-center text-gray-600">Loading achievement...</div>}
        {error && (
          <div className="p-4 bg-red-50 border border-red-200 rounded mb-4">
            <div className="text-red-700">{error}</div>
          </div>
        )}

        {achievement && (
          <div className="bg-white rounded-lg shadow p-6">
            <div className="mb-4">
              <h1 className="text-2xl font-bold text-gray-900">{achievement.title}</h1>
              <div className="text-sm text-gray-500 mt-1">{new Date(achievement.createdAt).toLocaleString()}</div>

              <div className="flex items-center gap-2 mt-3">
                <img
                  src={achievement.author?.profilePicture || fallbackAvatar(40)}
                  alt={achievement.author?.firstName || "User"}
                  className="w-10 h-10 rounded-full object-cover"
                  onError={(e) => {
                    e.currentTarget.onerror = null;
                    e.currentTarget.src = fallbackAvatar(40);
                  }}
                />
                <div>
                  <p className="text-sm font-semibold text-gray-900">{achievement.author?.firstName} {achievement.author?.lastName}</p>
                  <p className="text-xs text-gray-500">{achievement.author?.department}</p>
                </div>
              </div>
            </div>

            <div className="text-gray-700 mb-4 whitespace-pre-line">{achievement.description}</div>

            {achievement.tags && (
              <div className="flex gap-2 mb-4 flex-wrap">
                {achievement.tags.split(",").map((tag) => (
                  <span key={tag.trim()} className="px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded-full">{tag.trim()}</span>
                ))}
              </div>
            )}

            {achievement.media && achievement.media.length > 0 && (() => {
              const m = achievement.media[0] || {};
              const src = m.url || m.mediaUrl || m.fileUrl || m.path || m.src;
              const type = m.mediaType || m.mimeType || m.type || "";
              if (!src) return null;
              return (
                <div className="mb-4">
                  {type && type.startsWith("image") ? (
                    <img src={src} alt={achievement.title} className="w-full rounded object-cover max-h-96" />
                  ) : (
                    <video controls className="w-full rounded max-h-96">
                      <source src={src} type={type || "video/mp4"} />
                      Your browser does not support the video tag.
                    </video>
                  )}
                </div>
              );
            })()}

            <div className="mb-4">
              <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
                achievement.visibility === "PUBLIC" ? "bg-green-100 text-green-700" :
                achievement.visibility === "PRIVATE" ? "bg-red-100 text-red-700" :
                "bg-blue-100 text-blue-700"
              }`}>
                {achievement.visibility === "PUBLIC" && "🌐 Public"}
                {achievement.visibility === "PRIVATE" && "🔒 Private"}
                {achievement.visibility === "SHARED_WITH_TEAM" && "👥 Team Only"}
              </span>
            </div>

            <div className="mb-4 flex gap-4 text-sm text-gray-600">
              <span>❤️ {achievement.likeCount || 0} Likes</span>
              <span>💬 {achievement.commentCount || 0} Comments</span>
            </div>

            <div className="flex items-center gap-3">
              <button onClick={handleLike} className={`px-3 py-2 rounded font-medium transition ${achievement.isLikedByCurrentUser ? "bg-red-600 text-white" : "bg-gray-100 text-gray-700 hover:bg-gray-200"}`}>
                {achievement.isLikedByCurrentUser ? "❤️" : "🤍"} Like
              </button>

            </div>

            {/* Comments Section */}
            <div className="mt-6 bg-white">
              <h3 className="text-lg font-semibold mb-3">Comments ({achievement.commentCount || comments.length})</h3>

              <div className="mb-4">
                <textarea value={newComment} onChange={(e) => setNewComment(e.target.value)} placeholder="Write a comment..." className="w-full border border-gray-200 rounded p-2 text-sm" rows={3} />
                <div className="mt-2 flex items-center justify-end gap-2">
                  <button onClick={() => setNewComment("")} className="text-sm text-gray-600">Cancel</button>
                  <button onClick={handleAddComment} className="px-3 py-1 bg-blue-600 text-white rounded text-sm">Comment</button>
                </div>
              </div>

              {commentsLoading ? (
                <div className="text-sm text-gray-500">Loading comments...</div>
              ) : (
                <div className="space-y-4">
                  {comments.length === 0 && <div className="text-sm text-gray-500">No comments yet.</div>}
                  {comments.map((c) => {
                    const cid = c.commentId || c.id;
                    const isOwner = user && (user.id === (c.author?.id || c.authorId || c.userId));
                    const isHR = user && user.role === "HR";
                    return (
                      <div key={cid} className="flex items-start gap-3">
                        <img
                          src={c.author?.profilePicture || fallbackAvatar(36)}
                          alt={c.author?.firstName || "User"}
                          className="w-9 h-9 rounded-full object-cover"
                          onError={(e) => {
                            e.currentTarget.onerror = null;
                            e.currentTarget.src = fallbackAvatar(36);
                          }}
                        />
                        <div className="flex-1">
                          <div className="flex items-center justify-between">
                            <div>
                              <div className="text-sm font-semibold text-gray-900">{c.author?.firstName} {c.author?.lastName}</div>
                              <div className="text-xs text-gray-500">{new Date(c.createdAt).toLocaleString()}</div>
                            </div>
                            <div className="text-xs text-gray-400">{isOwner ? "You" : ""}</div>
                          </div>

                          <div className="mt-2 text-sm text-gray-700">
                            {editingCommentId === cid ? (
                              <>
                                <textarea className="w-full border border-gray-200 rounded p-2 text-sm" rows={3} value={editingText} onChange={(e) => setEditingText(e.target.value)} />
                                <div className="mt-2 flex gap-2">
                                  <button onClick={cancelEdit} className="text-sm text-gray-600">Cancel</button>
                                  <button onClick={() => submitEdit(c)} className="px-3 py-1 bg-blue-600 text-white rounded text-sm">Save</button>
                                </div>
                              </>
                            ) : (
                              <p className="whitespace-pre-line">{c.text || c.body}</p>
                            )}
                          </div>

                          <div className="mt-2 flex gap-3 text-xs text-gray-500">
                            {isOwner && editingCommentId !== cid && (
                              <button onClick={() => startEdit(c)} className="hover:underline">Edit</button>
                            )}
                            {(isOwner || isHR) && (
                              <button onClick={() => handleDeleteComment(c)} className="hover:underline text-red-600">Delete</button>
                            )}
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
};

export default AchievementDetails;
