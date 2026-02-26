import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import DashboardLayout from "../../layout/DashboardLayout";
import {
  toggleAchievementLike,
  fetchComments,
  addComment,
  deleteComment,
  deletePost,
  updatePost,
  moderateDeletePost,
  likeCommentHandle,
  getCommentLikeCount,
  deleteCommentHr
} from "../achievementsAPI";
import { useAuth } from "../../context/AuthContext";
import api from "../../api/axios";
import MediaCarousel from "../components/MediaCarousel";

const AchievementDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const fallbackAvatar = (size = 40) =>
    `data:image/svg+xml;utf8,
    <svg xmlns='http://www.w3.org/2000/svg' width='${size}' height='${size}'>
      <rect width='100%' height='100%' fill='%23e5e7eb'/>
      <text x='50%' y='50%' dominant-baseline='middle'
        text-anchor='middle'
        font-size='${Math.floor(size / 2.2)}'
        fill='%236b7280'>U</text>
    </svg>`;

  const [achievement, setAchievement] = useState(null);
  const [loading, setLoading] = useState(true);
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");

  const [isEditingPost, setIsEditingPost] = useState(false);
  const [editTitle, setEditTitle] = useState("");
  const [editDescription, setEditDescription] = useState("");

  /* ================= LOAD DATA ================= */

  useEffect(() => {
    if (!id) return;

    const load = async () => {
      const resp = await api.get(`/achievements/${id}`);
      const data = resp.data?.data || resp.data;
      setAchievement(data);
      setEditTitle(data.title);
      setEditDescription(data.description);
      setLoading(false);
    };

    const loadComments = async () => {
      const data = await fetchComments(id);
      const list = Array.isArray(data) ? data : data?.content || [];

      /* load like counts for each comment */
      const enriched = await Promise.all(
        list.map(async (c) => {
          const cid = c.id || c.commentId;
          const count = await getCommentLikeCount(cid);

          return {
            ...c,
            likeCount: count,
            isLiked: false // optional: backend can send this if you implement it
          };
        })
      );

      setComments(enriched);
    };

    load();
    loadComments();
  }, [id]);

  const isOwner = user && user.id === achievement?.author?.id;
  const isHR = user?.role === "ROLE_Hr";

  /* ================= POST LIKE ================= */

  const handleLike = async () => {
    await toggleAchievementLike(
      achievement.postId,
      achievement.isLikedByCurrentUser
    );

    setAchievement((prev) => ({
      ...prev,
      isLikedByCurrentUser: !prev.isLikedByCurrentUser,
      likeCount:
        (prev.likeCount || 0) +
        (prev.isLikedByCurrentUser ? -1 : 1)
    }));
  };

  /* ================= COMMENT LIKE ================= */

  const handleCommentLike = async (comment) => {
    const id = comment.id || comment.commentId;

     await likeCommentHandle(id,comment.isLikedByCurrentUser)

    setComments((prev) =>
      prev.map((c) =>
        (c.id || c.commentId) === id
          ? {
              ...c,
              isLikedByCurrentUser: !c.isLikedByCurrentUser,
              likeCount: c.likeCount + (c.isLikedByCurrentUser ? -1 : 1)
            }
          : c
      )
    );
  };

  /* ================= DELETE POST ================= */

  const handleOwnerDelete = async () => {
    if (!window.confirm("Delete your post?")) return;
    await deletePost(achievement.postId);
    navigate(-1);
  };

  const handleModerateDelete = async () => {
    const reason = window.prompt("Reason:");
    if (!reason) return;
    await moderateDeletePost(achievement.postId, reason);
    navigate(-1);
  };

  /* ================= UPDATE POST ================= */

  const handleUpdatePost = async () => {
    const resp = await updatePost(achievement.postId, {
      title: editTitle,
      description: editDescription,
      tags: achievement.tags,
      visibility: achievement.visibility
    });

    setAchievement(resp.data?.data || resp.data);
    setIsEditingPost(false);
  };

  /* ================= ADD COMMENT ================= */

  const handleAddComment = async () => {
    if (!newComment.trim()) return;

    const created = await addComment(
      achievement.postId,
      newComment.trim()
    );

    created.likeCount = 0;
    created.isLiked = false;

    setComments((prev) => [created, ...prev]);
    setNewComment("");
  };

  /* ================= DELETE COMMENT ================= */

  const handleDeleteComment = async (comment) => {
    const cid = comment.id || comment.commentId;
    await deleteComment(achievement.postId, cid);

    setComments((prev) =>
      prev.filter((c) => (c.id || c.commentId) !== cid)
    );
  };

    const handleDeleteModaretComment = async (comment) => {
    const cid = comment.id || comment.commentId;
    await deleteCommentHr(achievement.postId, cid);

    setComments((prev) =>
      prev.filter((c) => (c.id || c.commentId) !== cid)
    );
  };

  /* ================= LOADING ================= */

  if (loading)
    return (
      <DashboardLayout>
        <div className="py-20 text-center">Loading...</div>
      </DashboardLayout>
    );

  if (!achievement) return null;

  /* ================= UI ================= */

  return (
    <DashboardLayout>
      <div className="min-h-screen bg-gray-50 py-10 px-4">
        <div className="max-w-3xl mx-auto bg-white rounded-2xl shadow border overflow-hidden">

          {/* HEADER */}
          <div className="p-8 border-b relative">

            <div className="absolute top-6 right-6 flex gap-2">
              {isOwner && (
                <>
                  <button
                    onClick={() => setIsEditingPost(true)}
                    className="px-3 py-2 text-sm bg-blue-600 text-white rounded"
                  >
                    Edit
                  </button>
                  <button
                    onClick={handleOwnerDelete}
                    className="px-3 py-2 text-sm bg-red-600 text-white rounded"
                  >
                    Delete
                  </button>
                </>
              )}

              {!isOwner && isHR && (
                <button
                  onClick={handleModerateDelete}
                  className="px-3 py-2 text-sm bg-red-600 text-white rounded"
                >
                  Moderate Delete
                </button>
              )}
            </div>

            {isEditingPost ? (
              <>
                <input
                  value={editTitle}
                  onChange={(e) => setEditTitle(e.target.value)}
                  className="w-full border p-2 text-xl font-semibold rounded"
                />

                <textarea
                  value={editDescription}
                  onChange={(e) => setEditDescription(e.target.value)}
                  className="w-full border p-3 mt-4 rounded"
                  rows={5}
                />

                <div className="flex gap-3 mt-4">
                  <button
                    onClick={() => setIsEditingPost(false)}
                    className="px-4 py-2 bg-gray-200 rounded"
                  >
                    Cancel
                  </button>
                  <button
                    onClick={handleUpdatePost}
                    className="px-4 py-2 bg-blue-600 text-white rounded"
                  >
                    Save
                  </button>
                </div>
              </>
            ) : (
              <>
                <h1 className="text-3xl font-bold">
                  {achievement.title}
                </h1>

                <div className="text-sm text-gray-500 mt-2">
                  {new Date(
                    achievement.createdAt
                  ).toLocaleString()}
                </div>

                <p className="mt-6 whitespace-pre-line">
                  {achievement.description}
                </p>
              </>
            )}
          </div>

          {/* MEDIA */}
          <div className="p-8">
            {achievement.media?.length > 0 && (
              <MediaCarousel
                media={achievement.media}
                postId={achievement.postId}
              />
            )}

            {/* LIKE STATS */}
            <div className="flex justify-between mt-6 border-t pt-4">
              <div className="text-sm text-gray-600">
                ❤️ {achievement.likeCount}
              </div>

              <button
                onClick={handleLike}
                className={`px-4 py-2 rounded ${
                  achievement.isLikedByCurrentUser
                    ? "bg-red-600 text-white"
                    : "bg-gray-200"
                }`}
              >
                {achievement.isLikedByCurrentUser
                  ? "❤️ Liked"
                  : "🤍 Like"}
              </button>
            </div>

            {/* COMMENTS */}
            <div className="mt-10">
              <h3 className="font-semibold mb-4">Comments</h3>

              <textarea
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                className="w-full border rounded p-3"
              />

              <div className="flex justify-end mt-2">
                <button
                  onClick={handleAddComment}
                  className="px-4 py-2 bg-blue-600 text-white rounded"
                >
                  Comment
                </button>
              </div>

              <div className="space-y-6 mt-6">
                {comments.map((c) => {
                  const cid = c.id || c.commentId;

                  return (
                    <div key={cid} className="flex gap-3">
                      <img
                        src={
                          c.author?.profileImage ||
                          fallbackAvatar(40)
                        }
                        className="w-10 h-10 rounded-full"
                      />

                      <div className="flex-1 bg-gray-50 p-4 rounded-xl border">

                        <div className="font-semibold text-sm">
                          {c.author?.name}
                        </div>

                        <p className="text-sm mt-2">{c.text}</p>

                        <div className="flex gap-3 mt-3">

                          <button
                            onClick={() => handleCommentLike(c)}
                            className={`text-xs px-3 py-1 rounded ${
                              c.isLikedByCurrentUser
                                ? "bg-red-500 text-white"
                                : "bg-gray-200"
                            }`}
                          >
                            ❤️ {c.likeCount}
                          </button>

                        { isHR ? (
                            <button
                              onClick={() =>handleDeleteModaretComment(c)}
                              className="text-red-500 text-xs">
                              Modarate Delete
                            </button>
                           ):(user?.id === c.author?.id &&
                            (
                              <button onClick={() => handleDeleteComment(c)} className="text-red-500 text-xs">
                              Delete
                            </button>
                            )
                          )}
                          
                        </div>

                      </div>
                    </div>
                  );
                })}
              </div>
            </div>

          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default AchievementDetails;