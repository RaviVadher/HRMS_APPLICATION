import api from "../api/axios";

export const sendOtp = (userName) =>
{
  return  api.post(`/auth/fogot-password?userName=${userName}`);
}
 

export const verifyOtp = (userName, otp) =>
{
  return api.post(`/auth/verify-otp?userName=${userName}&otp=${otp}`);

}

export const resetPassword = (userName, newPassword) =>
{
  return api.post(`/auth/reset-password?userName=${userName}&newPassword=${newPassword}`);
}
