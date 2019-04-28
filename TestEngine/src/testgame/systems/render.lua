function getRequiredComponents()
  return {"position", "size", "color"}
end

function render(g)
  for i, entity in ipairs(entities) do
    pos = entity.position
    size = entity.size
    color = entity.color

    g:setColor(game.color:fromRGB(color.r, color.g, color.b))
    g:fillRect(pos.x, pos.y, size.width, size.height)
  end
end
